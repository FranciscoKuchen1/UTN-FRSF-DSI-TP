## Troubleshooting

### Common Issues:

#### 1. **Docker-Compose Fails to Start**
- **Cause**: Port conflicts or corrupted Docker volumes.
- **Solution**:
  ```bash
  docker compose down -v
  docker compose up
  ```

#### 2. **Angular Application Fails to Build**
- **Cause**: Missing modules or outdated dependencies.
- **Solution**:
  ```bash
  npm install
  ng serve
  ```

#### 3. **Backend Application Crashes**
- **Cause**: Misconfiguration in `application.yml`.
- **Solution**: Verify the configuration or use a local profile for debugging:
  ```bash
  -Dspring.profiles.active=local
