package com.uisleandro.store;

import android.content.Context;

import com.uisleandro.store.supply.sync.UnitOfflineDataSync;
import com.uisleandro.store.supply.sync.BrandOfflineDataSync;
import com.uisleandro.store.supply.sync.GenderOfflineDataSync;
import com.uisleandro.store.sales.sync.SaleTypeOfflineDataSync;
import com.uisleandro.store.core.sync.CurrencyOfflineDataSync;
import com.uisleandro.store.core.sync.TokenTypeOfflineDataSync;
import com.uisleandro.store.core.sync.RoleOfflineDataSync;
import com.uisleandro.store.client.sync.CountryOfflineDataSync;
import com.uisleandro.store.receivement.sync.BankOfflineDataSync;
import com.uisleandro.store.receivement.sync.InstallmentTypeOfflineDataSync;
import com.uisleandro.store.receivement.sync.InterestRateTypeOfflineDataSync;
import com.uisleandro.store.referral.sync.ResellerOfflineDataSync;
import com.uisleandro.store.core.sync.SystemOfflineDataSync;
import com.uisleandro.store.core.sync.UserOfflineDataSync;
import com.uisleandro.store.supply.sync.CategoryOfflineDataSync;
import com.uisleandro.store.cash.sync.CashRegisterOfflineDataSync;
import com.uisleandro.store.supply.sync.ProductOfflineDataSync;
import com.uisleandro.store.supply.sync.DistributorContactOfflineDataSync;
import com.uisleandro.store.credit_protection.sync.SharedClientOfflineDataSync;
import com.uisleandro.store.credit_protection.sync.IssueOfflineDataSync;
import com.uisleandro.store.client.sync.BasicClientOfflineDataSync;
import com.uisleandro.store.client.sync.ClientFromSystemOfflineDataSync;
import com.uisleandro.store.core.sync.DbLogOfflineDataSync;
import com.uisleandro.store.cash.sync.CashLaunchOfflineDataSync;
import com.uisleandro.store.core.sync.TokenOfflineDataSync;
import com.uisleandro.store.supply.sync.StockReviewOfflineDataSync;
import com.uisleandro.store.discount.sync.DiscountOfflineDataSync;
import com.uisleandro.store.sales.sync.SaleOfflineDataSync;
import com.uisleandro.store.sales.sync.ProductOnSaleOfflineDataSync;
import com.uisleandro.store.client.sync.BrazilianOfflineDataSync;
import com.uisleandro.store.receivement.sync.InvoiceOfflineDataSync;
import com.uisleandro.store.receivement.sync.BoletoSicoobOfflineDataSync;


import com.uisleandro.store.util.web.TLSUtils;
import com.uisleandro.store.util.web.TLSWebClient2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
Created by Uisleandro Costa dos Santos
uisleandro@gmail.com

This class is Responsible to update a Remote Database based on date In the local database
After comming offline. It's userfull if you're going to collect offline data from a phone
and then send it to a server.
*/
public class OfflineDatabaseUptader {

	TLSUtils utils;

	public OfflineDatabaseUptader(TLSUtils utils) {
		this.utils = utils;
	}

	public void Sync(){

		List<SyncUpdater> syncList = new ArrayList<>();

		Context context = utils.getContext();

		TLSWebClient2 client = new TLSWebClient2(utils);

		syncList.add(new UnitOfflineDataSync(client, context));
		syncList.add(new BrandOfflineDataSync(client, context));
		syncList.add(new GenderOfflineDataSync(client, context));
		syncList.add(new SaleTypeOfflineDataSync(client, context));
		syncList.add(new CurrencyOfflineDataSync(client, context));
		syncList.add(new TokenTypeOfflineDataSync(client, context));
		syncList.add(new RoleOfflineDataSync(client, context));
		syncList.add(new CountryOfflineDataSync(client, context));
		syncList.add(new BankOfflineDataSync(client, context));
		syncList.add(new InstallmentTypeOfflineDataSync(client, context));
		syncList.add(new InterestRateTypeOfflineDataSync(client, context));
		syncList.add(new ResellerOfflineDataSync(client, context));
		syncList.add(new SystemOfflineDataSync(client, context));
		// system points to: currency
		// system points to: reseller
		syncList.add(new UserOfflineDataSync(client, context));
		// user points to: system
		// user points to: role
		syncList.add(new CategoryOfflineDataSync(client, context));
		// category points to: category
		syncList.add(new CashRegisterOfflineDataSync(client, context));
		// cash_register points to: user
		syncList.add(new ProductOfflineDataSync(client, context));
		// product points to: system
		// product points to: gender
		// product points to: category
		// product points to: unit
		// product points to: brand
		syncList.add(new DistributorContactOfflineDataSync(client, context));
		// distributor_contact points to: brand
		syncList.add(new SharedClientOfflineDataSync(client, context));
		// shared_client points to: country
		syncList.add(new IssueOfflineDataSync(client, context));
		// Issue points to: shared_client
		// Issue points to: system
		syncList.add(new BasicClientOfflineDataSync(client, context));
		// basic_client points to: country
		syncList.add(new ClientFromSystemOfflineDataSync(client, context));
		// client_from_system points to: system
		// client_from_system points to: basic_client
		// client_from_system points to: shared_client
		// client_from_system points to: user
		syncList.add(new DbLogOfflineDataSync(client, context));
		// db_log points to: user
		syncList.add(new CashLaunchOfflineDataSync(client, context));
		// cash_launch points to: cash_register
		syncList.add(new TokenOfflineDataSync(client, context));
		// token points to: user
		// token points to: system
		// token points to: token_type
		syncList.add(new StockReviewOfflineDataSync(client, context));
		// stock_review points to: product
		syncList.add(new DiscountOfflineDataSync(client, context));
		// discount points to: product
		// discount points to: category
		// discount points to: brand
		// discount points to: client_from_system
		// discount points to: gender
		syncList.add(new SaleOfflineDataSync(client, context));
		// sale points to: sale_type
		// sale points to: system
		// sale points to: user
		// sale points to: client_from_system
		syncList.add(new ProductOnSaleOfflineDataSync(client, context));
		// product_on_sale points to: sale
		// product_on_sale points to: product
		syncList.add(new BrazilianOfflineDataSync(client, context));
		// brazilian points to: basic_client
		syncList.add(new InvoiceOfflineDataSync(client, context));
		// invoice points to: system
		// invoice points to: sale
		// invoice points to: client_from_system
		// invoice points to: installment_type
		// invoice points to: interest_rate_type
		// invoice points to: bank
		syncList.add(new BoletoSicoobOfflineDataSync(client, context));
		// boleto_sicoob points to: invoice


		Iterator<SyncUpdater> it;

		it = syncList.iterator();
		while(it.hasNext()){
			SyncUpdater that = it.next();
			that.insert_on_client();
		}

		it = syncList.iterator();
		while(it.hasNext()){
			SyncUpdater that = it.next();
			that.update_on_client();
		}

		it = syncList.iterator();
		while(it.hasNext()){
			SyncUpdater that = it.next();
			that.fix_foreign_keys_on_client();
		}

		it = syncList.iterator();
		while(it.hasNext()){
			SyncUpdater that = it.next();
			that.insert_on_server(); //foreign keys translated
		}
		it = syncList.iterator();
		while(it.hasNext()){
			SyncUpdater that = it.next();
			that.update_on_server(); //foreign keys translated
		}
	}

}