package com.bookstore_webapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookstore_webapp.clients.catalog.CatalogServiceClient;
import com.bookstore_webapp.clients.catalog.PagedResult;
import com.bookstore_webapp.clients.catalog.Product;

@Controller
class ProductController {
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

	private final CatalogServiceClient catalogService;

	ProductController(CatalogServiceClient catalogService) {
		this.catalogService = catalogService;
	}

	@GetMapping
	String index() {
		return "redirect:/products";
	}

	@GetMapping("/products")
	String productsPage(@RequestParam(name = "page", defaultValue = "1") int pageNo, Model model) {
		model.addAttribute("pageNo", pageNo);
		return "products";
	}

	@GetMapping("/api/products")
	@ResponseBody
	PagedResult<Product> products(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
		log.info("Fetching products for page: {}", page);
		return catalogService.getProducts(page);
	}
}
