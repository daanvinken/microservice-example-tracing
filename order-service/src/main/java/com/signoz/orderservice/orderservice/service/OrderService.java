package com.signoz.orderservice.orderservice.service;

import com.signoz.orderservice.orderservice.domain.UsersDTO;
import com.signoz.orderservice.orderservice.entity.Order;
import com.signoz.orderservice.orderservice.entity.User;
import com.signoz.orderservice.orderservice.feign.UserFeignClient;
import com.signoz.orderservice.orderservice.feign.PaymentFeignClient;
import com.signoz.orderservice.orderservice.repository.OrderRepository;
import com.signoz.orderservice.orderservice.repository.UserRepository;
import com.signoz.orderservice.orderservice.task.FileEncrypterDecrypterTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class OrderService {
    private final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private PaymentFeignClient paymentFeignClient;

    @Autowired
    private UserService userService;



    public Order save(int id, Order order){
        User userResponse = userFeignClient.getUsersById(id).getBody();
        Order orderData = new Order();
        orderData.setAccount(userResponse.getAccount());
        orderData.setOrderStatus("order_placed");
        orderData.setProductName(order.getProductName());
        orderData.setPrice(order.getPrice());
        Integer balanceAmount = Integer.parseInt(userResponse.getAmount()) - Integer.parseInt(order.getPrice());
        userResponse.setAmount(String.valueOf(balanceAmount));

        // Paymentservice
        Map<String,String> paymentResponse = paymentFeignClient.getPaymentByIdAmount(userResponse.getId(), "500").getBody();
        System.out.println(paymentResponse.toString());

        // Mocking some IO by encrypting/decrypting a file
        try {
            File inputFile = new File("/tmp/in.txt");
            String outputFilePath = "/tmp/out.txt";
            SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
            FileEncrypterDecrypterTask fileEncrypterDecrypter = new FileEncrypterDecrypterTask(secretKey, "AES/CBC/PKCS5Padding");
            fileEncrypterDecrypter.encrypt(inputFile, outputFilePath);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException ex) {
            LOGGER.warn(ex.toString());
        }


        // Update existing user balance
        User data = userService.save(userResponse);
        return orderRepository.save(orderData);
    }
}
