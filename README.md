# Quake Log Scraper
Tool that searches the log file of the game `Quake III Arena` and retrieves the score of each match 

## Getting Started
This project uses [Docker](https://www.docker.com/) to normalize Java dependencies without having to worry about
the machine's JVM.

To build an image, run the following command in the project's root folder:
```bash
docker build -t scraper .
```
> **Warning**
> If you receive an error indicating that the docker command was not recognized, it may mean that Docker is not installed 
> yet (make sure to restart the machine after installing Docker).

Once the image is created, let's create a volume to be able to share files from the host computer to the container:
```bash
docker run -v ./assets:/scraper/assets scraper
```

With the image and volume properly configured, simply access the container and run the application!
```bash
# To access the container's terminal
docker run -it scrapper

# Once inside the container's terminal, execute
scraper -f assets/qgames.log
```

## Adding other log files to extract data

Since the container does not have access to the local files on your computer, you need to add the new file
to a shared folder, which is the `assets` folder in the project's root directory.<br>
Once the file has been added, run the command:
```bash
# Inside the container's terminal, simply execute
scraper -f assets/<new file name>
```

## Arguments

To adapt the application to different situations, there are some mapped arguments:

| Argument | Abbreviation | Description                                                                   |
|----------|--------------|-------------------------------------------------------------------------------|
| --file   | -f           | Path to the log file                                                          |
| --type   | -t           | Defines the output format, can be JSON or REPORT (JSON is the default format) |
| --help   | -h           | Brief information about each argument                                         |
