package com.mycompany.myapp;

import com.mycompany.myapp.config.ApplicationProperties;
import com.mycompany.myapp.repository.RecordRepository;
import com.mycompany.myapp.service.PrinterSupport;
import com.mycompany.myapp.service.ReceiptPrint;
import com.mycompany.myapp.service.TestPrinter;
import io.jsonwebtoken.io.IOException;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationProperties.class })
public class ErApp {

    private static final Logger log = LoggerFactory.getLogger(ErApp.class);

    private final Environment env;

    public ErApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes er.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not " + "run with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     * @throws java.io.IOException
     * @throws FontFormatException
     */
    public static void main(String[] args) throws FontFormatException {
        SpringApplication app = new SpringApplication(ErApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
        /* final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream stream = loader.getResourceAsStream("font/Tajawal-Regular.ttf")) {
        	//Font  font = Font.createFont(Font.TRUETYPE_FONT, stream);
            //System.out.println("Font: " + font);

            
            GraphicsEnvironment ge = 
                GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {
				ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, stream));
			} catch (FontFormatException | java.io.IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         System.out.println(ge.getAllFonts());   
        } catch (java.io.IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();

        Font[] allFonts = ge.getAllFonts();

        for (Font font : allFonts) {
            System.out.println(font.getFontName(Locale.US));
        }*/
    }

    // @EventListener({ApplicationReadyEvent.class})
    private void applicationReadyEvent() {
        String url = "http://localhost:8080/";
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            String[] command;

            String operatingSystemName = System.getProperty("os.name").toLowerCase();
            if (operatingSystemName.indexOf("nix") >= 0 || operatingSystemName.indexOf("nux") >= 0) {
                String[] browsers = {
                    "opera",
                    "google-chrome",
                    "epiphany",
                    "firefox",
                    "mozilla",
                    "konqueror",
                    "netscape",
                    "links",
                    "lynx",
                };
                StringBuffer stringBuffer = new StringBuffer();

                for (int i = 0; i < browsers.length; i++) {
                    if (i == 0) stringBuffer.append(String.format("%s \"%s\"", browsers[i], url)); else stringBuffer.append(
                        String.format(" || %s \"%s\"", browsers[i], url)
                    );
                }
                command = new String[] { "sh", "-c", stringBuffer.toString() };
            } else if (operatingSystemName.indexOf("win") >= 0) {
                command = new String[] { "rundll32 url.dll,FileProtocolHandler " + url };
            } else if (operatingSystemName.indexOf("mac") >= 0) {
                command = new String[] { "open " + url };
            } else {
                System.out.println("an unknown operating system!!");
                return;
            }

            try {
                if (command.length > 1) runtime.exec(command); // linux
                else runtime.exec(command[0]); // windows or mac
            } catch (IOException e) {
                e.printStackTrace();
            } catch (java.io.IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            "\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}{}\n\t" +
            "External: \t{}://{}:{}{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }
}
