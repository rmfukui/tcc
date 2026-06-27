api_key = ENV.fetch("REDMINE_API_KEY")

Setting.rest_api_enabled = "1"
Setting.default_language = "en"

if Tracker.count.zero? || IssueStatus.count.zero? || IssuePriority.count.zero?
  Redmine::DefaultData::Loader.load("en")
end

admin = User.find_by(login: "admin")
if admin
  admin.password = "admin123"
  admin.password_confirmation = "admin123"
  admin.must_change_passwd = false if admin.respond_to?(:must_change_passwd=)
  admin.status = Principal::STATUS_ACTIVE
  admin.save!
end

user = admin || User.where(admin: true).first || User.first
raise "No Redmine user found to receive the demo API token" unless user

Token.where(user: user, action: "api").delete_all
api_token = Token.create!(user: user, action: "api")
api_token.update_column(:value, api_key)

role = Role.find_by(name: "Manager") || Role.where(builtin: 0).first
status = IssueStatus.find_by(is_closed: false) || IssueStatus.first
tracker = Tracker.find_by(name: "Feature") || Tracker.first
priority = IssuePriority.find_by(is_default: true) || IssuePriority.first

raise "Default Redmine data was not loaded" unless role && status && tracker && priority

project = Project.find_or_initialize_by(identifier: "tcc-demo")
project.name = "TCC Demo - Redmine Timer"
project.description = "Projeto de demonstracao para o TCC de log de horas integrado ao Redmine."
project.status = Project::STATUS_ACTIVE
project.is_public = true
project.save!
project.enabled_module_names = ["issue_tracking", "time_tracking"]
project.trackers = [tracker] unless project.trackers.exists?(tracker.id)

Member.find_or_create_by!(project: project, principal: user) do |member|
  member.roles = [role]
end

issues = [
  {
    subject: "Implementar tela de listagem de tarefas",
    description: "Criar a tela React que consome o endpoint GET /tasks.",
    estimated_hours: 4
  },
  {
    subject: "Criar fluxo de iniciar e pausar timer",
    description: "Permitir iniciar, pausar e retomar o apontamento de horas.",
    estimated_hours: 6
  },
  {
    subject: "Enviar apontamento de horas para o Redmine",
    description: "Integrar o fechamento do timer com a API POST /time_entries.json.",
    estimated_hours: 5
  },
  {
    subject: "Montar historico de apontamentos",
    description: "Exibir logs sincronizados para facilitar a demonstracao do MVP.",
    estimated_hours: 3
  }
]

issues.each do |data|
  issue = Issue.find_or_initialize_by(project: project, subject: data[:subject])
  issue.tracker = tracker
  issue.status = status
  issue.priority = priority
  issue.author = user
  issue.assigned_to = user
  issue.description = data[:description]
  issue.estimated_hours = data[:estimated_hours]
  issue.save!
end

puts "Redmine demo seed completed. URL: http://localhost:3000 | API key: #{api_key}"
