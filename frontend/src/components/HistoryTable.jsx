import RefreshIcon from '@mui/icons-material/Refresh';
import {
  Box,
  Card,
  CardContent,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Tooltip,
  Typography,
} from '@mui/material';
import { formatDateTime, formatHours } from '../utils/formatters';

export default function HistoryTable({ history, loading, onRefresh }) {
  return (
    <Card component="section">
      <CardContent>
        <Box className="section-title-row history-title">
          <Box>
            <Typography variant="h6">Historico</Typography>
            <Typography variant="body2" color="text.secondary">
              Apontamentos enviados ao Redmine
            </Typography>
          </Box>
          <Tooltip title="Atualizar historico">
            <span>
              <IconButton aria-label="Atualizar historico" onClick={onRefresh} disabled={loading}>
                <RefreshIcon />
              </IconButton>
            </span>
          </Tooltip>
        </Box>

        <TableContainer component={Paper} className="history-table" variant="outlined">
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Redmine</TableCell>
                <TableCell>Issue</TableCell>
                <TableCell>Horas</TableCell>
                <TableCell>Comentario</TableCell>
                <TableCell>Sincronizado em</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {history.map((item) => (
                <TableRow key={item.id}>
                  <TableCell>#{item.redmineEntryId || '-'}</TableCell>
                  <TableCell>#{item.issueId}</TableCell>
                  <TableCell>{formatHours(item.hours)}</TableCell>
                  <TableCell>{item.comments}</TableCell>
                  <TableCell>{formatDateTime(item.syncedAt)}</TableCell>
                </TableRow>
              ))}
              {history.length === 0 && (
                <TableRow>
                  <TableCell colSpan={5}>
                    <Typography variant="body2" color="text.secondary">
                      Nenhum apontamento sincronizado.
                    </Typography>
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </CardContent>
    </Card>
  );
}
