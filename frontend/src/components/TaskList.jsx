import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import RefreshIcon from '@mui/icons-material/Refresh';
import {
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  Divider,
  IconButton,
  List,
  ListItem,
  ListItemText,
  Stack,
  Tooltip,
  Typography,
} from '@mui/material';

export default function TaskList({
  tasks,
  loading,
  activeTimer,
  onRefresh,
  onStart,
}) {
  const hasActiveTimer = activeTimer && activeTimer.status !== 'FINISHED';

  return (
    <Card component="section" className="task-panel">
      <CardContent>
        <Stack spacing={2}>
          <Box className="section-title-row">
            <Box>
              <Typography variant="h6">Tarefas</Typography>
              <Typography variant="body2" color="text.secondary">
                Issues atribuidas ao usuario no Redmine
              </Typography>
            </Box>
            <Tooltip title="Atualizar tarefas">
              <span>
                <IconButton aria-label="Atualizar tarefas" onClick={onRefresh} disabled={loading}>
                  <RefreshIcon />
                </IconButton>
              </span>
            </Tooltip>
          </Box>

          <List disablePadding className="task-list">
            {tasks.map((task, index) => (
              <Box key={task.id}>
                <ListItem
                  disableGutters
                  secondaryAction={
                    <Button
                      variant="outlined"
                      startIcon={<PlayArrowIcon />}
                      onClick={() => onStart(task)}
                      disabled={hasActiveTimer}
                    >
                      Iniciar
                    </Button>
                  }
                >
                  <ListItemText
                    primary={
                      <Stack direction="row" spacing={1} alignItems="center" className="task-title">
                        <Typography component="span" fontWeight={700}>
                          #{task.id}
                        </Typography>
                        <Typography component="span">{task.subject}</Typography>
                      </Stack>
                    }
                    secondary={
                      <Stack direction="row" spacing={1} useFlexGap flexWrap="wrap" sx={{ mt: 1 }}>
                        <Chip size="small" label={task.projectName} />
                        <Chip size="small" label={task.statusName} color="primary" variant="outlined" />
                        <Chip size="small" label={task.trackerName} variant="outlined" />
                      </Stack>
                    }
                  />
                </ListItem>
                {index < tasks.length - 1 && <Divider />}
              </Box>
            ))}
          </List>

          {!loading && tasks.length === 0 && (
            <Typography variant="body2" color="text.secondary">
              Nenhuma tarefa encontrada.
            </Typography>
          )}
        </Stack>
      </CardContent>
    </Card>
  );
}
