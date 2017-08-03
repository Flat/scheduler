SELECT U03oxz.appointment.appointmentId,
         U03oxz.customer.customerId,
         U03oxz.appointment.title AS AppointmentTitle,
         U03oxz.appointment.description AS AppointmentDescription,
         U03oxz.appointment.location AS AppointmentLocation,
         U03oxz.appointment.contact AS AppointmentContact,
         U03oxz.appointment.url AS AppointmentUrl,
         U03oxz.appointment.start AS AppointmentStart,
         U03oxz.appointment.end AS AppointmentEnd,
         U03oxz.appointment.createDate AS AppointmentCreateDate,
         U03oxz.appointment.createdBy AS AppointmentCreatedBy,
         U03oxz.appointment.lastUPDATE AS AppointmentLastUpdate,
         U03oxz.appointment.lastUpdateBy AS AppointmentLastUpdateBy,
         U03oxz.customer.customerName AS CustomerName,
         U03oxz.customer.addressId as AddressId
FROM U03oxz.appointment
INNER JOIN U03oxz.customer
    ON U03oxz.appointment.customerId = U03oxz.customer.customerId
INNER JOIN U03oxz.address
    ON U03oxz.customer.addressId = U03oxz.address.addressId
INNER JOIN U03oxz.city
    ON U03oxz.address.cityId = U03oxz.city.cityId
INNER JOIN U03oxz.country
    ON U03oxz.city.countryId = U03oxz.country.countryId
WHERE U03oxz.appointment.createdBy = "test"AND DATE_FORMAT(U03oxz.appointment.start, "%m") = 08
        AND DATE_FORMAT(U03oxz.appointment.start, "%Y") = 2017;