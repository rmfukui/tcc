import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import LoginIcon from '@mui/icons-material/Login';
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  Stack,
  TextField,
  Typography,
} from '@mui/material';

export default function ConnectionPanel({
  form,
  connectedUser,
  loading,
  error,
  onChange,
  onSubmit,
}) {
  return (
    <Card component="section">
      <CardContent>
        <Stack spacing={2.5}>
          <Box className="section-title-row">
            <Box>
              <Typography variant="h6">Conexao Redmine</Typography>
              <Typography variant="body2" color="text.secondary">
                Ambiente de demonstracao do MVP
              </Typography>
            </Box>
            {connectedUser && (
              <Chip
                color="primary"
                icon={<CheckCircleIcon />}
                label={connectedUser}
                variant="outlined"
              />
            )}
          </Box>

          {error && <Alert severity="error">{error}</Alert>}

          <Box component="form" onSubmit={onSubmit} className="connection-form">
            <TextField
              fullWidth
              label="URL do Redmine"
              name="redmineUrl"
              value={form.redmineUrl}
              onChange={onChange}
              size="small"
            />
            <TextField
              fullWidth
              label="API Key"
              name="apiKey"
              value={form.apiKey}
              onChange={onChange}
              size="small"
              type="password"
            />
            <Button
              type="submit"
              variant="contained"
              startIcon={<LoginIcon />}
              disabled={loading}
            >
              Validar
            </Button>
          </Box>
        </Stack>
      </CardContent>
    </Card>
  );
}
