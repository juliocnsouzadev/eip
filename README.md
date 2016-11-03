# Enterprise Integration Patterns Examples

## File Transfer

### Problema
Como integrar múltiplas aplicações para que possam trabalhar juntas e trocar informações?

### Solução
Faça  com que  cada aplicação  produza  arquivos que  contenham  a  informação que  a outra aplicação
precisa consumir. Integradores assumem a responsabilidade de transformar os arquivos em diferentes
formatos. Produza os arquivos em intervalos regulares de acordo com a natureza do negócio.
