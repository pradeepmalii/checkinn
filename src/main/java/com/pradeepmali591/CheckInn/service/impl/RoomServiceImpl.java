package com.pradeepmali591.CheckInn.service.impl;

import com.pradeepmali591.CheckInn.dto.room.request.RoomRequest;
import com.pradeepmali591.CheckInn.dto.room.response.RoomResponse;
import com.pradeepmali591.CheckInn.entity.Hotel;
import com.pradeepmali591.CheckInn.entity.Room;
import com.pradeepmali591.CheckInn.exception.ResourceNotFoundException;
import com.pradeepmali591.CheckInn.repository.HotelRepository;
import com.pradeepmali591.CheckInn.repository.RoomRepository;
import com.pradeepmali591.CheckInn.service.RoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RoomServiceImpl implements RoomService {

    RoomRepository roomRepository;
    HotelRepository hotelRepository;
    ModelMapper modelMapper;

    @Override
    public RoomResponse createNewRoom(Long hotelId, RoomRequest request) {

        log.info("Creating a new Room in hotel with ID: {}", hotelId);

        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        Room room = modelMapper.map(request, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        log.info("Created new Room in hotel with ID: {}", hotelId);

        //TODO: create inventory as soon as room is created and if hotel is active

        return modelMapper.map(room, RoomResponse.class);
    }

    @Override
    public List<RoomResponse> getAllRoomsInHotel(Long hotelId) {

        log.info("Getting list of all room in with ID: {}",hotelId);

        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        log.info("Got List of Room in hotel with ID: {}", hotelId);

        return hotel.getRooms()
                .stream()
                .map((elements) -> modelMapper.map(elements, RoomResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        log.info("Getting room with ID: {}",roomId);

        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: "+roomId));

        log.info("Got room with ID: {}",roomId);

        return modelMapper.map(room, RoomResponse.class);
    }

    @Override
    public void deleteRoomById(Long roomId) {

        log.info("Deleting room with ID: {}", roomId);

        boolean exists = roomRepository.existsById(roomId);
        if(!exists){
            throw new ResourceNotFoundException("Room not found with ID: "+roomId);
        }

        roomRepository.deleteById(roomId);

        //TODO: delete all future inventory for this room


    }
}
