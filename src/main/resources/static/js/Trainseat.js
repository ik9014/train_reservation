function removeSeatsByUniqueKey(uniqueKey, carriageNum, seat) {
    console.log(uniqueKey + ", " + carriageNum + ", " + seat);

    const key = `unavailableSeats_${uniqueKey}_${carriageNum}`;
    console.log("key:" + key);

    const seatToRemove = `${uniqueKey}-${carriageNum}-${seat}`;
    console.log("seatToRemove: "  + seatToRemove)

    // 로컬스토리지에서 해당 키 값을 가져옵니다.
    let unavailableSeats = JSON.parse(window.localStorage.getItem(key))
    console.log("unavailableSeats: " + unavailableSeats)

    // 배열에서 seatToRemove 항목을 제거
    unavailableSeats = unavailableSeats.filter(seat => seat !== seatToRemove);
    console.log("Updated unavailableSeats: ", unavailableSeats);

    // 삭제된 항목이 있으면 로컬스토리지에 저장
    if (unavailableSeats.length < JSON.parse(localStorage.getItem(key)).length) {
        localStorage.setItem(key, JSON.stringify(unavailableSeats));
        console.log(`Removed seat: ${seatToRemove}. Updated unavailable seats:`, unavailableSeats);
    } else {
        console.log(`Seat ${seatToRemove} was not found in the list.`);
    }
}

function selectCarriage(carriageNumber, uniqueKey) {
    console.log(`UniqueKey: ${uniqueKey}, CarriageNumber: ${carriageNumber}`); // 확인 로그

    document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active'));
    document.querySelector(`.tab:nth-child(${carriageNumber})`).classList.add('active');

    generateSeats(uniqueKey, carriageNumber);
}

// 좌석 데이터를 렌더링
function generateSeats(uniqueKey, carriageNumber) {
    console.log(`Generating seats for UniqueKey: ${uniqueKey}, Carriage: ${carriageNumber}`); // 확인 로그

    const seatMap = document.getElementById('seat-map');
    seatMap.innerHTML = ''; // 기존 좌석 초기화

    const seatData = getSeatData(uniqueKey, carriageNumber);
    console.log('Seat Data:', seatData); // 데이터 확인

    seatData.forEach(seat => {
        const seatDiv = document.createElement('div');
        seatDiv.classList.add('seat');
        seatDiv.textContent = seat.display; // 화면 표시용 좌석 번호
        seatDiv.dataset.seatId = seat.id;  // 내부 데이터에 고유 ID 저장

        if (isSeatUnavailable(uniqueKey, carriageNumber, seat.id)) {
            seatDiv.classList.add('unavailable');
        } else {
            seatDiv.addEventListener('click', () => toggleSeat(seatDiv, uniqueKey, carriageNumber));
        }

        seatMap.appendChild(seatDiv);
    });
}

function toggleSeat(seat, uniqueKey, carriageNumber) {
    const selectedSeats = document.querySelectorAll('.seat.selected');

    if (seat.classList.contains('selected')) {
        seat.classList.remove('selected');
    } else if (selectedSeats.length < passengerCount) {
        seat.classList.add('selected');
    } else {
        alert(`최대 ${passengerCount}개의 좌석만 선택할 수 있습니다.`);
    }

    updateSelectedSeatsDisplay();
}


function updateSelectedSeatsDisplay() {
    const selectedSeats = Array.from(document.querySelectorAll('.seat.selected')).map(seat => seat.textContent.trim());
    document.getElementById('selected-seats-list').textContent = selectedSeats.join(', ') || '없음';
    document.getElementById('submitBtn').disabled = selectedSeats.length === 0;
}


function submitSeats() {
    const selectedSeats = Array.from(document.querySelectorAll('.seat.selected')).map(seat => seat.dataset.seatId);

    if (selectedSeats.length === 0) {
        alert('좌석을 선택해주세요.');
        return;
    }

    const carriageNumber = getActiveCarriage();
    saveUnavailableSeats(uniqueKey, carriageNumber, selectedSeats);

    const selectedSeatsInput = document.getElementById('selectedSeatsInput');
    selectedSeatsInput.value = selectedSeats.join(',');

    const form = document.querySelector('form');
    form.submit();
}

function saveSelectedSeats(uniqueKey, carriageNumber, selectedSeats) {
    const key = `selectedSeats_${uniqueKey}_${carriageNumber}`;
    localStorage.setItem(key, JSON.stringify(selectedSeats));
    console.log(`Saved seats: ${selectedSeats} with key: ${key}`);
}

function getSeatData(uniqueKey, carriageNumber) {
    const defaultSeats = [
        '1A', '2A', '3A', '4A', '5A', '6A', '7A', '8A',
        '1B', '2B', '3B', '4B', '5B', '6B', '7B', '8B'
    ];

    if (!uniqueKey || !carriageNumber) {
        console.error(`Invalid UniqueKey (${uniqueKey}) or CarriageNumber (${carriageNumber})`);
        return []; // 오류 발생 시 빈 배열 반환
    }

    // 고유 키와 호차 번호 기반 데이터 생성
    return defaultSeats.map(seat => ({
        id: `${uniqueKey}-${carriageNumber}-${seat}`, // 고유 ID
        display: seat                                // 화면 표시용 좌석 번호
    }));
}

function isSeatUnavailable(uniqueKey, carriageNumber, seatId) {
    const key = `unavailableSeats_${uniqueKey}_${carriageNumber}`;
    const unavailableSeats = JSON.parse(localStorage.getItem(key)) || [];
    console.log(`Unavailable seats for key ${key}:`, unavailableSeats);
    return unavailableSeats.includes(seatId);
}

function saveUnavailableSeats(uniqueKey, carriageNumber, seats) {
    const key = `unavailableSeats_${uniqueKey}_${carriageNumber}`;
    let unavailableSeats = JSON.parse(localStorage.getItem(key)) || [];
    unavailableSeats = [...new Set([...unavailableSeats, ...seats])]; // 중복 제거
    localStorage.setItem(key, JSON.stringify(unavailableSeats));
    console.log(`Saved unavailable seats: ${unavailableSeats} with key: ${key}`);
}

function getActiveCarriage() {
    return Array.from(document.querySelectorAll('.tab')).findIndex(tab => tab.classList.contains('active')) + 1;
}

function goToPreviousPage() {
    window.history.back();
}

// 초기화
selectCarriage(1, uniqueKey);
