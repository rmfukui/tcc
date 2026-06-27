import AccessTimeIcon from '@mui/icons-material/AccessTime';
import AssignmentIcon from '@mui/icons-material/Assignment';
import {
  Alert,
  AppBar,
  Box,
  Container,
  LinearProgress,
  Snackbar,
  Stack,
  Toolbar,
  Typography,
} from '@mui/material';
import { useCallback, useEffect, useMemo, useState } from 'react';
import {
  finishTimer,
  listHistory,
  listTasks,
  pauseTimer,
  resumeTimer,
  startTimer,
  validateRedmine,
} from './api/redmineTimerApi';
import { getErrorMessage } from './api/http';
import ConnectionPanel from './components/ConnectionPanel';
import HistoryTable from './components/HistoryTable';
import TaskList from './components/TaskList';
import TimerPanel from './components/TimerPanel';

const defaultConnection = {
  redmineUrl: 'http://redmine:3000',
  apiKey: '0123456789abcdef0123456789abcdef01234567',
};

function calculateDisplaySeconds(timer) {
  if (!timer) {
    return 0;
  }

  if (timer.status !== 'RUNNING') {
    return timer.elapsedSeconds;
  }

  const startedAt = new Date(timer.startTime).getTime();
  const elapsedSinceStart = Math.max(0, Math.floor((Date.now() - startedAt) / 1000));
  return timer.elapsedSeconds + elapsedSinceStart;
}

export default function App() {
  const [connectionForm, setConnectionForm] = useState(defaultConnection);
  const [connectedUser, setConnectedUser] = useState('');
  const [tasks, setTasks] = useState([]);
  const [history, setHistory] = useState([]);
  const [activeTimer, setActiveTimer] = useState(null);
  const [displaySeconds, setDisplaySeconds] = useState(0);
  const [comments, setComments] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const isBusy = useMemo(() => loading, [loading]);

  const runAction = useCallback(async (action, successMessage) => {
    setLoading(true);
    setError('');
    try {
      const result = await action();
      if (successMessage) {
        setMessage(successMessage);
      }
      return result;
    } catch (actionError) {
      setError(getErrorMessage(actionError));
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  const loadTasks = useCallback(async () => {
    const result = await runAction(listTasks);
    if (result) {
      setTasks(result);
    }
  }, [runAction]);

  const loadHistory = useCallback(async () => {
    const result = await runAction(listHistory);
    if (result) {
      setHistory(result);
    }
  }, [runAction]);

  useEffect(() => {
    loadTasks();
    loadHistory();
  }, [loadTasks, loadHistory]);

  useEffect(() => {
    setDisplaySeconds(calculateDisplaySeconds(activeTimer));

    if (!activeTimer || activeTimer.status !== 'RUNNING') {
      return undefined;
    }

    const intervalId = window.setInterval(() => {
      setDisplaySeconds(calculateDisplaySeconds(activeTimer));
    }, 1000);

    return () => window.clearInterval(intervalId);
  }, [activeTimer]);

  function handleConnectionChange(event) {
    const { name, value } = event.target;
    setConnectionForm((current) => ({ ...current, [name]: value }));
  }

  async function handleValidate(event) {
    event.preventDefault();

    const result = await runAction(
      () => validateRedmine(connectionForm),
      'Configuracao validada com sucesso.',
    );

    if (result) {
      setConnectedUser(result.username);
      await loadTasks();
    }
  }

  async function handleStart(task) {
    const result = await runAction(
      () => startTimer({ issueId: task.id, issueName: task.subject }),
      'Timer iniciado.',
    );

    if (result) {
      setActiveTimer(result);
      setComments(`Trabalho realizado na tarefa #${task.id}`);
    }
  }

  async function handlePause() {
    const result = await runAction(pauseTimer, 'Timer pausado.');
    if (result) {
      setActiveTimer(result);
    }
  }

  async function handleResume() {
    const result = await runAction(resumeTimer, 'Timer retomado.');
    if (result) {
      setActiveTimer(result);
    }
  }

  async function handleFinish() {
    const result = await runAction(
      () => finishTimer({ comments }),
      'Horas registradas no Redmine.',
    );

    if (result) {
      setActiveTimer(null);
      setComments('');
      await loadHistory();
      await loadTasks();
    }
  }

  return (
    <Box className="app-shell">
      <AppBar position="static" color="inherit" elevation={0} className="app-bar">
        <Toolbar className="toolbar">
          <Box className="brand-mark">
            <AccessTimeIcon />
          </Box>
          <Box>
            <Typography variant="h6">Redmine Timer</Typography>
            <Typography variant="body2" color="text.secondary">
              Gerenciamento de log de horas
            </Typography>
          </Box>
        </Toolbar>
        {loading && <LinearProgress />}
      </AppBar>

      <Container maxWidth="xl" className="main-container">
        {error && (
          <Alert severity="error" onClose={() => setError('')} className="top-alert">
            {error}
          </Alert>
        )}

        <Box className="dashboard-grid">
          <Box className="left-column">
            <ConnectionPanel
              form={connectionForm}
              connectedUser={connectedUser}
              loading={isBusy}
              error=""
              onChange={handleConnectionChange}
              onSubmit={handleValidate}
            />
            <TimerPanel
              activeTimer={activeTimer}
              displaySeconds={displaySeconds}
              comments={comments}
              loading={isBusy}
              onCommentsChange={setComments}
              onPause={handlePause}
              onResume={handleResume}
              onFinish={handleFinish}
            />
          </Box>

          <Box className="right-column">
            <TaskList
              tasks={tasks}
              loading={isBusy}
              activeTimer={activeTimer}
              onRefresh={loadTasks}
              onStart={handleStart}
            />
          </Box>
        </Box>

        <HistoryTable history={history} loading={isBusy} onRefresh={loadHistory} />

        <Box className="footer-note">
          <AssignmentIcon fontSize="small" />
          <Typography variant="body2">
            MVP integrado ao Redmine local criado pelo Docker Compose.
          </Typography>
        </Box>
      </Container>

      <Snackbar
        open={Boolean(message)}
        autoHideDuration={3000}
        onClose={() => setMessage('')}
        message={message}
      />
    </Box>
  );
}
