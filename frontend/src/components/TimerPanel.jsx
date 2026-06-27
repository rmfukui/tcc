import DoneIcon from '@mui/icons-material/Done';
import PauseIcon from '@mui/icons-material/Pause';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import {
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  Stack,
  TextField,
  Typography,
} from '@mui/material';
import { formatSeconds } from '../utils/formatters';

export default function TimerPanel({
  activeTimer,
  displaySeconds,
  comments,
  loading,
  onCommentsChange,
  onPause,
  onResume,
  onFinish,
}) {
  const isRunning = activeTimer?.status === 'RUNNING';
  const isPaused = activeTimer?.status === 'PAUSED';

  return (
    <Card component="section">
      <CardContent>
        <Stack spacing={2.5}>
          <Box className="section-title-row">
            <Box>
              <Typography variant="h6">Timer</Typography>
              <Typography variant="body2" color="text.secondary">
                Controle do apontamento atual
              </Typography>
            </Box>
            <Chip
              color={isRunning ? 'primary' : isPaused ? 'secondary' : 'default'}
              label={activeTimer?.status || 'IDLE'}
              variant={activeTimer ? 'filled' : 'outlined'}
            />
          </Box>

          <Box className="timer-display">
            <Typography variant="body2" color="text.secondary">
              Tempo acumulado
            </Typography>
            <Typography className="timer-value">{formatSeconds(displaySeconds)}</Typography>
          </Box>

          <Box>
            <Typography variant="body2" color="text.secondary">
              Tarefa atual
            </Typography>
            <Typography fontWeight={700}>
              {activeTimer ? `#${activeTimer.issueId} - ${activeTimer.issueName}` : 'Nenhuma tarefa em andamento'}
            </Typography>
          </Box>

          <TextField
            label="Comentario do apontamento"
            value={comments}
            onChange={(event) => onCommentsChange(event.target.value)}
            minRows={3}
            multiline
            disabled={!activeTimer}
          />

          <Stack direction="row" spacing={1} useFlexGap flexWrap="wrap">
            {isRunning && (
              <Button variant="outlined" startIcon={<PauseIcon />} onClick={onPause} disabled={loading}>
                Pausar
              </Button>
            )}
            {isPaused && (
              <Button variant="outlined" startIcon={<PlayArrowIcon />} onClick={onResume} disabled={loading}>
                Retomar
              </Button>
            )}
            <Button
              variant="contained"
              color="secondary"
              startIcon={<DoneIcon />}
              onClick={onFinish}
              disabled={!activeTimer || loading}
            >
              Finalizar
            </Button>
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  );
}
