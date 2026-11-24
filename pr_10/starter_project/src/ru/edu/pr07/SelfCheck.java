package ru.edu.pr07;

public class SelfCheck {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        DeliveryService service = new DeliveryService(new DeliveryPolicyFactory());

        check("PICKUP sample cost = 0",
                () -> approx(service.calculate(
                        new DeliveryRequest(DeliveryType.PICKUP, 0, 10, 8000)), 0.0));

        check("STANDARD sample cost = 227",
                () -> approx(service.calculate(
                        new DeliveryRequest(DeliveryType.STANDARD, 12, 4.5, 12000)), 227.0));

        check("EXPRESS sample cost = 389.4",
                () -> approx(service.calculate(
                        new DeliveryRequest(DeliveryType.EXPRESS, 5, 1.2, 4000)), 389.4));

        check("CARGO upper weight 35 included",
                () -> approx(service.calculate(
                        new DeliveryRequest(DeliveryType.CARGO, 10, 35, 1000)), 550.0));

        check("CARGO upper weight 35.0001 rejected",
                () -> throwsDeliveryUnavailable(() -> service.calculate(
                        new DeliveryRequest(DeliveryType.CARGO, 10, 35.0001, 1000))));

        check("CARGO weight 0 rejected by request validation",
                () -> throwsIllegalArgument(() -> new DeliveryRequest(
                        DeliveryType.CARGO, 10, 0, 1000)));

        check("null type rejected",
                () -> throwsIllegalArgument(() -> new DeliveryRequest(
                        null, 10, 5, 1000)));

        check("PICKUP with distance 1 rejected",
                () -> throwsDeliveryUnavailable(() -> service.calculate(
                        new DeliveryRequest(DeliveryType.PICKUP, 1, 10, 1000))));

        check("STANDARD weight 20 included",
                () -> approx(service.calculate(
                        new DeliveryRequest(DeliveryType.STANDARD, 1, 20, 1000)), 210.0));

        check("STANDARD weight 20.0001 rejected",
                () -> throwsDeliveryUnavailable(() -> service.calculate(
                        new DeliveryRequest(DeliveryType.STANDARD, 1, 20.0001, 1000))));

        System.out.println("PASSED=" + passed + " FAILED=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void check(String name, CheckedBoolean body) {
        try {
            boolean ok = body.get();
            if (ok) {
                passed++;
                System.out.println("PASS " + name);
            } else {
                failed++;
                System.out.println("FAIL " + name);
            }
        } catch (Exception e) {
            failed++;
            System.out.println("FAIL " + name + " exception "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private static boolean approx(double actual, double expected) {
        return Math.abs(actual - expected) < 1e-9;
    }

    private static boolean throwsDeliveryUnavailable(CheckedRunnable body) {
        try {
            body.run();
            return false;
        } catch (DeliveryUnavailableException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean throwsIllegalArgument(CheckedRunnable body) {
        try {
            body.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @FunctionalInterface
    private interface CheckedBoolean {
        boolean get() throws Exception;
    }

    @FunctionalInterface
    private interface CheckedRunnable {
        void run() throws Exception;
    }
}
