package hello;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import hello.model.Person;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class LDIFView extends VerticalLayout {

	private final LDIFRepository repo;

	private final CustomerEditor editor;

	final Grid<Person> grid;

	final TextField filter;

	private final Button addNewBtn;

	public LDIFView(LDIFRepository repo, CustomerEditor editor) {
		
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(Person.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		add(actions, grid, editor);
		
		grid.setWidth("75%");
		grid.setHeight("300px");
		//HeaderRow headerRow = grid.prependHeaderRow(); //"UID", "Full Name", "Last Name", "Org Unit");
		//headerRow.
		
		grid.setColumns("id", "firstName", "lastName");
		grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

		filter.setPlaceholder("Filter by last name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.EAGER);
		filter.addValueChangeListener(e -> listPersons(e.getValue()));

		// Connect selected Customer to editor or hide if none is selected
//		grid.asSingleSelect().addValueChangeListener(e -> {
//			editor.editCustomer(e.getValue());
//		});

		// Instantiate and edit new Customer the new button is clicked
//		addNewBtn.addClickListener(e -> editor.editCustomer(new Customer("", "")));

		// Listen changes made by the editor, refresh data from backend
/*		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCustomers(filter.getValue());
		});*/

		// Initialize listing
		listPersons(null);
	}

	// tag::listCustomers[]
/*	void listCustomers(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(repo.findAll());
		}
		else {
			grid.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
		}
	}*/
	// end::listCustomers[]
	
	void listPersons(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(repo.getPersonRepo().findAll());
		}
		else {
			grid.setItems(repo.getPersonRepo().findAll());
			//grid.setItems(repo.findByLastNameStartsWithIgnoreCase(filterText));
		}
	}
	// end::listCustomers[]
	
}