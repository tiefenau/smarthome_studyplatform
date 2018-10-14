FROM node:latest
RUN mkdir /usr/src/app
WORKDIR /usr/src/app
RUN npm install -g @angular/cli@6.1.5
COPY . /usr/src/app
CMD ng serve --host 0.0.0.0 --port 4200