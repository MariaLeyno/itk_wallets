package org.mashal.tasks.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Wallet API",
                version = "${api.version}",
                contact = @Contact(name = "Maria Leyno", email = "leyno.masha@gmail.com"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
                description = "Wallets REST API that allows to manage wallet operations and get actual wallet amount"
        )
)
public class OpenAPIConfiguration {
}
