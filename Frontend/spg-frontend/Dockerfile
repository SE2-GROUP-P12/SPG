# pull official base image
FROM node:16

# set working directory
WORKDIR /app

# add `/app/node_modules/.bin` to $PATH
ENV PATH /app/node_modules/.bin:$PATH

# install app dependencies
COPY package.docker.json ./package.json
COPY package-lock.json ./
RUN npm install --silent
# RUN npm install react-scripts@3.4.1 -g --silent

# add app
COPY ./src/ ./src
COPY ./public/ ./public

CMD ["npm", "start"]

# TODO: Add a build stage to have a better and safe deployment of the image