package ru.cetelem.supplier.ui.view;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.vaadin.klaudeta.PaginatedGrid;
import org.vaadin.crudui.crud.impl.GridCrud;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

import ru.cetelem.cassiope.supplier.model.CarModel;
import ru.cetelem.cassiope.supplier.model.Dealer;
import ru.cetelem.cassiope.supplier.model.FinancePlan;
import ru.cetelem.supplier.service.DealerService;
import ru.cetelem.supplier.service.DictionaryService;
import ru.cetelem.supplier.ui.component.XFGridCrud;

@PageTitle("Dictionary editor")

@Route(value = "dic", layout = MainLayout.class)
public class DictionaryEditorView extends BaseView implements RouterLayout,  HasUrlParameter<String>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	private DictionaryService dictionaryService;
	private DealerService dealerService;
	
	private  GridCrud<?> grid;
	
	@Autowired
	public DictionaryEditorView(DictionaryService dictionaryService, 
			DealerService dealerService) {
		this.dictionaryService = dictionaryService;
		this.dealerService = dealerService;

	}




	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String dicrionaryName) {
		if(grid!=null)
			remove(grid);
			 
		switch(dicrionaryName) {
		case "model":
			grid = createModelGrid();
	
			break;
		case "dealers":
			grid = createDealerGrid();
	
			break;
		case "plan":
			grid = createFinancePlanGrid();
	
			break;
		default:
			return;
		}
		
		add(grid); 

		grid.setSizeFull(); 
		setSizeFull();

	}
	



	private GridCrud<CarModel> createModelGrid() {
	
		GridCrud<CarModel> currentGrid = new GridCrud<>(CarModel.class);
		getUI().ifPresent(ui->ui.getPage().setTitle("Models"));
	
		currentGrid.setAddOperation(dictionaryService::carModelAdd);
		currentGrid.setDeleteOperation(dictionaryService::carModelDelete);
		currentGrid.setUpdateOperation(dictionaryService::carModelUpdate);
		currentGrid.setFindAllOperation(dictionaryService::getModels);
	
		return currentGrid;
	}
	
	private GridCrud<Dealer> createDealerGrid() {
		GridCrud<Dealer> currentGrid = new XFGridCrud<>(Dealer.class);
		getUI().ifPresent(ui->ui.getPage().setTitle("Dealers"));

		currentGrid.getUpdateButton().addClickListener(e->{
			Optional<Dealer> selectedDealer = currentGrid.getGrid().getSelectedItems().stream().findFirst();
			if(selectedDealer.isPresent())
				getUI().ifPresent(ui -> ui.navigate("dealer/" + selectedDealer.get().getCode()));				
		});
		
		currentGrid.getAddButton().addClickListener(e->{
			getUI().ifPresent(ui -> ui.navigate("dealer"));				
		});
		
		//currentGrid.setAddOperation(dealerService.dealerRepository::save);
		currentGrid.setDeleteOperation(dealerService.dealerRepository::delete);
		//currentGrid.setUpdateOperation(dealerService.dealerRepository::save);
		currentGrid.setFindAllOperation(dealerService::getDealers);
		currentGrid.getGrid().setColumns("code", "name"/*, 
				"limit.hardLimit", "limit.softLimit", "limit.totalFinanced", "limit.availableAmount",
				"limit.sublimitDeCc", "limit.totalFinancedDeCc", "limit.availableAmountDeCc"*/);
		
		Locale ruLocale = new Locale("ru");
		NumberFormat numberFormat = NumberFormat.getNumberInstance(ruLocale);
		numberFormat.setMinimumFractionDigits(2);

	    currentGrid.getGrid()
	    	.addColumn(new NumberRenderer<> (d->{return d.getLimit().getHardLimit();}, numberFormat, ""))
	        .setHeader("Hard Limit")
			.setComparator((v1, v2) -> (v1.getLimit().getHardLimit().compareTo(v2.getLimit().getHardLimit())))
			.setTextAlign(ColumnTextAlign.END)
	        .setKey("Hard Limit")
			.setSortOrderProvider(d -> Arrays.asList(new QuerySortOrder("Hard Limit", d)).stream());
	    currentGrid.getGrid()	       
	    	.addColumn(new NumberRenderer<> (d->{return d.getLimit().getSoftLimit();}, numberFormat, ""))
	        .setHeader("Soft Limit")
			.setComparator((v1, v2) -> (v1.getLimit().getSoftLimit().compareTo(v2.getLimit().getSoftLimit())))
			.setTextAlign(ColumnTextAlign.END)
	        .setKey("Soft Limit")
			.setSortOrderProvider(d -> Arrays.asList(new QuerySortOrder("Soft Limit", d)).stream());
	    currentGrid.getGrid()	       
	    	.addColumn(new NumberRenderer<> (d->{return d.getLimit().getTotalFinanced();}, numberFormat, ""))
	        .setHeader("Total Financed")
			.setComparator((v1, v2) -> (v1.getLimit().getTotalFinanced().compareTo(v2.getLimit().getTotalFinanced())))
			.setTextAlign(ColumnTextAlign.END)
	        .setKey("Total Financed")
			.setSortOrderProvider(d -> Arrays.asList(new QuerySortOrder("Total Financed", d)).stream());
	    currentGrid.getGrid()	       
	    	.addColumn(new NumberRenderer<> (d->{return d.getLimit().getAvailableAmount();}, numberFormat, ""))
	        .setHeader("Available Amount")
			.setComparator((v1, v2) -> (v1.getLimit().getAvailableAmount().compareTo(v2.getLimit().getAvailableAmount())))
			.setTextAlign(ColumnTextAlign.END)
	        .setKey("Available Amount")
			.setSortOrderProvider(d -> Arrays.asList(new QuerySortOrder("Available Amount", d)).stream());

		currentGrid.getGrid().getColumnByKey("code").setResizable(true);
		currentGrid.getGrid().getColumnByKey("name").setResizable(true);
		currentGrid.getGrid().getColumnByKey("Hard Limit").setResizable(true);
		currentGrid.getGrid().getColumnByKey("Soft Limit").setResizable(true);
		currentGrid.getGrid().getColumnByKey("Total Financed").setResizable(true);
		currentGrid.getGrid().getColumnByKey("Available Amount").setResizable(true);
/*	    
	    currentGrid.getGrid()	       
	    	.addColumn(new NumberRenderer<> (d->{return d.getLimit().getSublimitDeCc();}, numberFormat, ""))
	        .setHeader("Sublimit DeCc")
	        .setTextAlign(ColumnTextAlign.END)
	        .setSortable(true);
	    currentGrid.getGrid()	       
	    	.addColumn(new NumberRenderer<> (d->{return d.getLimit().getTotalFinancedDeCc();}, numberFormat, ""))
	        .setHeader("Total Financed DE CC")
	        .setTextAlign(ColumnTextAlign.END);
	    currentGrid.getGrid()	       
	    	.addColumn(new NumberRenderer<> (d->{return d.getLimit().getAvailableAmountDeCc();}, numberFormat, ""))
	        .setHeader("Available Amount DE CC")
	        .setTextAlign(ColumnTextAlign.END);
*/
	    
		currentGrid.getCrudFormFactory().setVisibleProperties("code", "name");	
		
		return currentGrid;
	}
	
	private GridCrud<FinancePlan> createFinancePlanGrid() {
		GridCrud<FinancePlan> currentGrid = new GridCrud<>(FinancePlan.class);
		getUI().ifPresent(ui->ui.getPage().setTitle("Plans"));
		
		currentGrid.setAddOperation(dictionaryService.financePlanRepository::save);
		currentGrid.setDeleteOperation(dictionaryService.financePlanRepository::delete);
		currentGrid.setUpdateOperation(dictionaryService.financePlanRepository::save);
		currentGrid.setFindAllOperation(dictionaryService::getFinancePlanes);
	
		return currentGrid;
	}


}
