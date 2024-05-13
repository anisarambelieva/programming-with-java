package org.store.services;

import org.store.models.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

public class ReceiptService {
    private Receipt receipt;

    public ReceiptService(Receipt receipt) {
        this.receipt = receipt;
    }

    public BigDecimal calculateTotalPrice() {
        BigDecimal result = BigDecimal.ZERO;

        for (Map.Entry<Goods, Double> entry : this.receipt.getGoods().entrySet()) {
            Goods goods = entry.getKey();
            double quantity = entry.getValue();

            BigDecimal price = goods.getPrice();
            BigDecimal totalGoodPrice = price.multiply(BigDecimal.valueOf(quantity));

            result = result.add(totalGoodPrice);
        }

        return result.setScale(2, BigDecimal.ROUND_UP);
    }

    public int countReceiptsIssued() {
        String directoryPath = "src/main/resources/receipts";
        File folder = new File(directoryPath);
        int fileCount = 0;

        if (folder.exists()) {
            File[] files = folder.listFiles();

            if (files.length > 0) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileCount++;
                    }
                }
            }
        }

        return fileCount;
    }
}
