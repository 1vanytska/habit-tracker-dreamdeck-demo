FROM node:22.22-alpine AS  build
WORKDIR /app
COPY package*.json ./
RUN --mount=type=cache,target=/root/.npm npm ci
COPY . .
RUN npm run build
#  --configuration=production

FROM nginx:1.28.2-alpine3.23 AS production
COPY nginx.conf /etc/nginx/nginx.conf
# COPY --chown=nginx:nginx --from=build /app/dist/frontend/browser/. /usr/share/nginx/html/
# RUN cp /usr/share/nginx/html/index.csr.html /usr/share/nginx/html/index.html
COPY --chown=nginx:nginx --from=build /app/dist/frontend/browser/. /usr/share/nginx/html/
USER nginx
ENTRYPOINT ["nginx", "-c", "/etc/nginx/nginx.conf"]
CMD ["-g", "daemon off;"]