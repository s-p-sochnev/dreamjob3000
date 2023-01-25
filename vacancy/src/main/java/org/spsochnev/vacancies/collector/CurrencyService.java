package org.spsochnev.vacancies.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CurrencyService {

    @Autowired
    private CurrencySource currencySource;

    @Autowired
    private CurrencyRepository currencyRepository;

    private static Map<String, Currency> currencyCache = new HashMap<>();

    public void updateCurrencies() {
        Map<String, BigDecimal> exchangeRates = currencySource.getCurrencyExchangeRates(currencyCache.keySet());
        for (Map.Entry<String, BigDecimal> entry: exchangeRates.entrySet()) {
            currencyCache.get(entry.getKey()).setExchangeRate(entry.getValue());
        }
        writeCurrenciesToDB(currencyCache.values());
    }

    private void writeCurrenciesToDB(Collection<Currency> currencies) {
        currencies.forEach(currency -> {
            String code = currency.getCode();
            Optional<Currency> o = currencyRepository.findByCode(code);
            Currency updatedCurrency = currency;
            if (o.isPresent()) {
                updatedCurrency = o.get();
                updatedCurrency.setExchangeRate(currency.getExchangeRate());
            }
            currencyRepository.save(updatedCurrency);
        });
    }

    private Currency saveCurrency(String code) {
        Currency currency = new Currency(code);

        Optional<Currency> o = currencyRepository.findByCode(code);
        Currency currencyWithID = o.orElseGet(() -> currencyRepository.save(currency));
        return currencyCache.put(code, currencyWithID);
    }

    public Currency getCurrencyByCode(String code) {
        if (currencyCache.containsKey(code)) return currencyCache.get(code);
        return  saveCurrency(code);
    }

}
