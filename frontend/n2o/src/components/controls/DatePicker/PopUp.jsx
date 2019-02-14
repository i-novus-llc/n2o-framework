import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';

import Calendar from './Calendar';
import { parseDate } from './utils';
import DateTimeControl from './DateTimeControl';

/**
 * Компонент PopUp
 * @reactProps {moment} value
 * @reactProps {string} dateFormat
 * @reactProps {string} timeFormat
 * @reactProps {function} markTimeAtSet
 * @reactProps {function} inTimeSet
 * @reactProps {boolean} auto
 * @reactProps {function} select
 * @reactProps {function} setPlacement
 * @reactProps {function} setVisibility
 * @reactProps {string} placement
 * @reactProps {moment} max
 * @reactProps {moment} min
 * @reactProps {string} locale
 * @reactProps {object} time
 */
class PopUp extends React.PureComponent {
  render() {
    const {
      dateFormat,
      markTimeAsSet,
      timeFormat,
      value,
      max,
      min,
      locale,
      time,
      isTimeSet
    } = this.props;

    let minDate = inputName => {
      if (value[DateTimeControl.beginInputName] && inputName === DateTimeControl.endInputName) {
        return parseDate(value[DateTimeControl.beginInputName], dateFormat);
      }
      return parseDate(min, dateFormat);
    };

    return (
      <div className="n2o-pop-up n2o-picker-dropdown position-absolute d-inline-flex justify-content-end">
        {Object.keys(value).map((input, i) => {
          const { hasDefaultTime, ...timeObj } = time[input];
          return (
            <Calendar
              key={i}
              time={timeObj}
              markTimeAsSet={markTimeAsSet}
              hasDefaultTime={hasDefaultTime || isTimeSet[input]}
              inputName={input}
              value={value[input]}
              timeFormat={timeFormat}
              select={this.props.select}
              setVisibility={this.setVisibility}
              max={parseDate(max, dateFormat)}
              min={minDate(input)}
              locale={locale}
            />
          );
        })}
      </div>
    );
  }
}

PopUp.propTypes = {
  value: PropTypes.instanceOf(moment),
  dateFormat: PropTypes.string,
  timeFormat: PropTypes.string,
  markTimeAsSet: PropTypes.func,
  inTimeSet: PropTypes.func,
  auto: PropTypes.bool,
  select: PropTypes.func,
  setPlacement: PropTypes.func,
  setVisibility: PropTypes.func,
  placement: PropTypes.string,
  max: PropTypes.instanceOf(moment),
  min: PropTypes.instanceOf(moment),
  locale: PropTypes.oneOf(['en', 'ru']),
  time: PropTypes.shape({
    mins: PropTypes.number,
    hours: PropTypes.number
  })
};

export default PopUp;
