package com.sunshireshuttle.driver.model;

public class TripLogBean extends BaseBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String idtrip_history_log;
    String trip_id;
    String employee_id;
    String employee_type;
    String message_type;
    String message_text;
    String reg_date;

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getIdtrip_history_log() {
        return idtrip_history_log;
    }

    public void setIdtrip_history_log(String idtrip_history_log) {
        this.idtrip_history_log = idtrip_history_log;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_type() {
        return employee_type;
    }

    public void setEmployee_type(String employee_type) {
        this.employee_type = employee_type;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }
    //    "idtrip_history_log": "13",
//            "trip_id": "2016120100455813",
//            "employee_id": "13",
//            "employee_type": "DRIVER",
//            "message_type": "GAS_POINT",
//            "message_text": "[Chris Test] added $ 45 gas",
//            "reg_date": "2016-11-30 09:46:10",

//            "employee_data": {
//        "status": true,
//                "message": "Driver found",
//                "driver": {
//            "0": "13",
//                    "1": "chris",
//                    "2": "78faea7d364ea6d8a7b44567d3103575",
//                    "3": "Chris Test",
//                    "4": "8145664700",
//                    "5": "",
//                    "6": "chrisyao900908@gmail.com",
//                    "7": "",
//                    "8": "",
//                    "9": "31563446",
//                    "10": "0000-00-00",
//                    "11": "",
//                    "12": "1",
//                    "13": "17",
//                    "14": "2016-06-24 10:06:15",
//                    "15": "1",
//                    "id": "13",
//                    "username": "chris",
//                    "password": "78faea7d364ea6d8a7b44567d3103575",
//                    "name": "Chris Test",
//                    "phone": "8145664700",
//                    "phoneb": "",
//                    "email": "chrisyao900908@gmail.com",
//                    "emailb": "",
//                    "emergency_phone": "",
//                    "dl_num": "31563446",
//                    "dl_exp": "0000-00-00",
//                    "note": "",
//                    "status": "1",
//                    "cby": "17",
//                    "cdate": "2016-06-24 10:06:15",
//                    "onfile": "1"
//        }
//    }

}
