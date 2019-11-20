package org.sid.lightecomv1.web;

import java.util.Date;

import javax.transaction.Transactional;

import org.sid.lightecomv1.dao.ClientRepository;
import org.sid.lightecomv1.dao.OrderItemRepository;
import org.sid.lightecomv1.dao.OrderRepository;
import org.sid.lightecomv1.dao.PayementRepository;
import org.sid.lightecomv1.dao.ProductRepository;
import org.sid.lightecomv1.entities.Client;
import org.sid.lightecomv1.entities.Order;
import org.sid.lightecomv1.entities.OrderItem;
import org.sid.lightecomv1.entities.Payment;
import org.sid.lightecomv1.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class OrderController {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	PayementRepository payementReposirory;

	Order order=new Order();

	@Transactional
	@PostMapping("/orders")
	public Order saveOrder(@RequestBody OrderForm orderForm) {
		Client client = new Client();
		client.setName(orderForm.getClient().getName());
		client.setEmail(orderForm.getClient().getEmail());
		client.setAddress(orderForm.getClient().getAddress());
		client.setPhoneNumber(orderForm.getClient().getPhoneNumber());
		client.setUsername(orderForm.getClient().getUsername());
		client = clientRepository.save(client);
		System.out.println(client.getId());

		order.setClient(client);
		order.setDate(new Date());
		order = orderRepository.save(order);
		double total = 0;
		for (OrderProduct p : orderForm.getProducts()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			Product product = productRepository.findById(p.getId()).get();
			orderItem.setProduct(product);
			orderItem.setPrice(product.getCurrentPrice());
			orderItem.setQuantity(p.getQuantity());
			orderItemRepository.save(orderItem);
			total += p.getQuantity() * product.getCurrentPrice();
		}
		order.setTotalAmount(total);
		return orderRepository.save(order);
	}

	@PutMapping("/orders")
	public boolean setOrder(@RequestBody Payment payment) {
		orderRepository.setOrder(payment, order.getId());
		return true;
	}
}
