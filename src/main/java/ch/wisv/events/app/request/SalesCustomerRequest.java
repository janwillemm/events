package ch.wisv.events.app.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Copyright (c) 2016  W.I.S.V. 'Christiaan Huygens'
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
@NoArgsConstructor
public class SalesCustomerRequest {

    @Getter
    @Setter
    private String orderReference;

    @Getter
    @Setter
    private String customerName;

    @Getter
    @Setter
    private String customerEmail;

    @Getter
    @Setter
    private String customerCHUsername;

    @Getter
    @Setter
    private String customerRFIDToken;

    public SalesCustomerRequest(String orderReference) {
        this.orderReference = orderReference;
    }
}
