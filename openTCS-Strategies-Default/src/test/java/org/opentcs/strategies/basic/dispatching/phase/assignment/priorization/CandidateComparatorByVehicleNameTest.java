/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.strategies.basic.dispatching.phase.assignment.priorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.theInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.DriveOrder;
import org.opentcs.data.order.Route;
import org.opentcs.data.order.TransportOrder;
import org.opentcs.strategies.basic.dispatching.AssignmentCandidate;
import org.opentcs.strategies.basic.dispatching.priorization.candidate.CandidateComparatorByVehicleName;

/**
 *
 * @author Mustafa Yalciner (Fraunhofer IML)
 */
public class CandidateComparatorByVehicleNameTest {

  private CandidateComparatorByVehicleName comparator;

  @BeforeEach
  public void setUp() {
    comparator = new CandidateComparatorByVehicleName();
  }

  @Test
  public void sortVehicleNamesAlphabetically() {
    AssignmentCandidate candidate1 = candidateWithVehicleName("AA");
    AssignmentCandidate candidate2 = candidateWithVehicleName("C");
    AssignmentCandidate candidate3 = candidateWithVehicleName("AB");

    List<AssignmentCandidate> list = new ArrayList<>();
    list.add(candidate1);
    list.add(candidate2);
    list.add(candidate3);

    Collections.sort(list, comparator);

    assertThat(list.get(0), is(theInstance(candidate1)));
    assertThat(list.get(1), is(theInstance(candidate3)));
    assertThat(list.get(2), is(theInstance(candidate2)));
  }

  private AssignmentCandidate candidateWithVehicleName(String vehicleName) {
    TransportOrder trasportOrder = new TransportOrder("TOrder1", new ArrayList<>());
    Route.Step dummyStep
        = new Route.Step(null, null, new Point("Point1"), Vehicle.Orientation.FORWARD, 1);
    Route route = new Route(Arrays.asList(dummyStep), 10);
    List<DriveOrder> driveOrders = List.of(
        new DriveOrder(new DriveOrder.Destination(new Point("Point2").getReference()))
            .withRoute(route)
    );

    return new AssignmentCandidate(new Vehicle(vehicleName), trasportOrder, driveOrders);
  }
}
