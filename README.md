## Spring Boot microservice that consumes an external API
I'll be calling Spreedly's payment processing API in this example. I'll have a REST controller that will take a credit card and that will be tokenized by Spreedly.
Spreedly (www.spreedly.com) is a fast reliable PCI compliant Payment processor. 
* Using Spring WebFlux WebClient to call Spreedly, no RestTemplate
* Using WireMock for Integration tests calling Spreedly (yes, Integration tests do no mock but I'm going to because
at times it is better to decouple from services you do not own)
* MockMvc will help Integration test my REST controller
 