import React from 'react';
import DatePicker from './DatePicker';
import DateInterval from './DateInterval';
import Calendar from './Calendar';

import moment from 'moment';

class DatePickerTest extends React.Component {
  constructor() {
    super();
  }

  render() {
    return (
      <div>
        <div style={{ position: 'fixed', top: '350px', width: '100%' }}>
          <DateInterval min={moment()} />
        </div>

        <div style={{ position: 'fixed', top: '50px', width: '100%' }}>
          <DatePicker defaultTime="12:11" />
        </div>
      </div>
    );
  }
}

export default DatePickerTest;
