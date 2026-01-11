# Theta-web-automation

## Installation

### Prerequisites

1. **Java 21 or higher**

   - Download and install from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
   - Verify installation: `java -version`

2. **Maven**

   - Download and install from [Apache Maven](https://maven.apache.org/download.cgi)
   - Verify installation: `mvn -version`

3. **Web Project**

   - Web project given in the assignment should be up and running locally on the port 3000.
   - Project link : (https://bitbucket.org/0xmvptech/ctrading/src/main/)

### Configuration

Configure the browser settings in `src/main/java/config/config.ini`:

- Set `browser` under `[environment]` section (e.g., `chrome`, `firefox`, `safari`)
- Adjust browser settings in `[browser]` section as needed

### Running Tests

To run the tests in chrome, execute this command : `mvn clean install`

To run the tests in safari, first change the browser in config file then follow the below steps :

1. Open Safari
2. Go to Safari → Settings (or Cmd + ,)
3. Click the Advanced tab
4. Check "Show Develop menu in menu bar" (if not already checked)
5. Go to Develop menu → Check "Allow Remote Automation"
6. Allow Remote Automation
   Execute this command and enter the mac login screen password : ` sudo safaridriver --enable && mvn clean test`

[Note] : If any popup comes on safari while automation, select allow Remote Automation
