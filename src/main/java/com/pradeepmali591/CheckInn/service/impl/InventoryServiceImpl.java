package com.pradeepmali591.CheckInn.service.impl;

import com.pradeepmali591.CheckInn.dto.hotel.response.HotelResponse;
import com.pradeepmali591.CheckInn.dto.hotelSearch.request.HotelSearchRequest;
import com.pradeepmali591.CheckInn.entity.Hotel;
import com.pradeepmali591.CheckInn.entity.Inventory;
import com.pradeepmali591.CheckInn.entity.Room;
import com.pradeepmali591.CheckInn.repository.InventoryRepository;
import com.pradeepmali591.CheckInn.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InventoryServiceImpl implements InventoryService {

    InventoryRepository inventoryRepository;
    ModelMapper modelMapper;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for(; !today.isAfter(endDate); today = today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .SurgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();

            inventoryRepository.save(inventory);
        }

    }

    @Override
    public void deleteAllInventories(Room room){
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByRoom(room);

    }

    @Override
    public Page<HotelResponse> searchHotels(HotelSearchRequest request) {

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        long dateCount =
                ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

        Page<Hotel> hotelPage = inventoryRepository
                .findHotelsWithAvailableInventory(
                        request.getCity(),
                        request.getStartDate(),
                        request.getEndDate(),
                        request.getRoomsCount(),
                        dateCount,
                        pageable);

        return hotelPage.map((element) -> modelMapper.map(element, HotelResponse.class));
    }
}
