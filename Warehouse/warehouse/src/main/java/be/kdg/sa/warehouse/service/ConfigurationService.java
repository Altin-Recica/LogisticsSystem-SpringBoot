package be.kdg.sa.warehouse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConfigurationService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    @Value("${kdg.commission.rate}")
    private double commissionRate;

    @Value("${kdg.storage.costs.gips}")
    private double gipsStorageCost;

    @Value("${kdg.storage.costs.ijzererts}")
    private double ijzerertsStorageCost;

    @Value("${kdg.storage.costs.cement}")
    private double cementStorageCost;

    @Value("${kdg.storage.costs.petcoke}")
    private double petcokeStorageCost;

    @Value("${kdg.storage.costs.slak}")
    private double slakStorageCost;

    @Value("${kdg.selling.price.gips}")
    private double gipsSellingPrice;

    @Value("${kdg.selling.price.ijzererts}")
    private double ijzerertsSellingPrice;

    @Value("${kdg.selling.price.cement}")
    private double cementSellingPrice;

    @Value("${kdg.selling.price.petcoke}")
    private double petcokeSellingPrice;

    @Value("${kdg.selling.price.slak}")
    private double slakSellingPrice;

    @Value("${kdg.max.capacity}")
    private double maxCapacity;

    @Value("${kdg.overflow.capacity}")
    private double overflowCapacity;

    @Value("${kdg.purchase.order.max}")
    private double purchaseOrderMax;

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public double getOverflowCapacity() {
        return overflowCapacity;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public double getPurchaseOrderMax() {
        return purchaseOrderMax;
    }

    public double getStorageCost(String materialType) {
        return switch (materialType.toLowerCase()) {
            case "gips" -> gipsStorageCost;
            case "ijzererts" -> ijzerertsStorageCost;
            case "cement" -> cementStorageCost;
            case "petcoke" -> petcokeStorageCost;
            case "slak" -> slakStorageCost;
            default -> {
                logger.error("Unknown material type for storage cost: {}", materialType);
                yield 0.0;
            }
        };
    }

    public double getSellingPrice(String materialType) {
        return switch (materialType.toLowerCase()) {
            case "gips" -> gipsSellingPrice;
            case "ijzererts" -> ijzerertsSellingPrice;
            case "cement" -> cementSellingPrice;
            case "petcoke" -> petcokeSellingPrice;
            case "slak" -> slakSellingPrice;
            default -> {
                logger.error("Unknown material type for selling prices: {}", materialType);
                yield 0.0;
            }
        };
    }

    public Map<String, Double> getSellingPrices() {
        return Map.of(
                "Gips", gipsSellingPrice,
                "Ijzererts", ijzerertsSellingPrice,
                "Cement", cementSellingPrice,
                "Petcoke", petcokeSellingPrice,
                "Slak", slakSellingPrice
        );
    }

    public Map<String, Double> getStorageCosts() {
        return Map.of(
                "Gips", gipsStorageCost,
                "Ijzererts", ijzerertsStorageCost,
                "Cement", cementStorageCost,
                "Petcoke", petcokeStorageCost,
                "Slak", slakStorageCost
        );
    }

}
