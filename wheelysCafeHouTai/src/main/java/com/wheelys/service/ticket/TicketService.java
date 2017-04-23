package com.wheelys.service.ticket;

import java.util.List;
import java.util.Map;

import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.pay.ElecCardBatch;

public interface TicketService {

	List<ElecCardBatch> findAllTicketList(Integer pageNo, String name, int rowsPerPage);

	int findAllCount(String name);

	ElecCardBatch getTicketDetail(Long id);

	CafeItem getcafeItemListById(Long long1);

	void updateTicket(Long id);

	void updateTicket(ElecCardBatch elecCardBatch);
	
	String jsonTomapTokey(String json);
	
	Map<CafeItem, List<CafeProduct>> itemproductMap(String producrids);
	/**
	 * 通过id查找店铺
	 * @param shopid
	 * @return
	 */
	List<String>shopnames(Long id);
	/**
	 * 通过id查找商品
	 * @param id
	 * @return
	 */
	List<Long>productids(Long id);
	/**
	 * 通过id查找商品
	 * @param id
	 * @return
	 */
	List<Long>caitmsids(Long id);
}
