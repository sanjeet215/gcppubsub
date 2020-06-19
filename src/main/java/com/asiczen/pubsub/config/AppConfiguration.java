package com.asiczen.pubsub.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class AppConfiguration {

	@Bean
	public PubSubInboundChannelAdapter messageChannelAdapter(
			@Qualifier("pubsubInputChannel") MessageChannel inputChannel, PubSubTemplate pubSubTemplate) {
		System.out.println("I am executed");
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "spring-cleint");
		adapter.setOutputChannel(inputChannel);
		adapter.setAckMode(AckMode.MANUAL);

		return adapter;
	}

	@Bean
	public MessageChannel pubsubInputChannel() {
		return new DirectChannel();
	}

	// @Bean
	// @ServiceActivator(inputChannel = "pubsubInputChannel")
	// public MessageHandler messageReceiver() {
	// return message -> {
	// System.out.println("Message arrived! Payload: " + new String((byte[])
	// message.getPayload()));
	// BasicAcknowledgeablePubsubMessage originalMessage = message.getHeaders()
	// .get(GcpPubSubHeaders.ORIGINAL_MESSAGE,
	// BasicAcknowledgeablePubsubMessage.class);
	// originalMessage.ack();
	// };
	// }

	@ServiceActivator(inputChannel = "pubsubInputChannel")
	public void messageReceiver(String payload) {
		System.out.println("Message arrived! Payload: " + payload);
	}
}
