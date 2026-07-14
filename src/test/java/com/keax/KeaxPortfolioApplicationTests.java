package com.keax;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba de humo que verifica que todo el contexto Spring, sus beans, seguridad
 * y repositorios pueden inicializarse con la configuracion de pruebas.
 */
@SpringBootTest
class KeaxPortfolioApplicationTests {

	@Test
	void contextLoads() {
		// Assert implicito: si una dependencia o configuracion falla, el contexto no carga.
	}

}
