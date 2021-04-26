import React from 'react'
import PropTypes from 'prop-types'
import moment from 'moment'
import pick from 'lodash/pick'

import DateInput from './DateInput'

/**
 * @reactProps {string} dateFormat
 * @reactProps {boolean} disabled
 * @reactProps {string} placeholder
 * @reactProps {moment} value
 * @reactProps {function} onInputChange
 * @reactProps {function} setVisibility
 * @reactProps {boolean} openOnFocus
 */
class DateInputGroup extends React.Component {
    constructor(props) {
        super(props)
    }

    /**
   * рендер полей-инпутов
   */
    renderInputs() {
        const {
            dateFormat,
            disabled,
            placeholder,
            value,
            inputClassName,
            onInputChange,
            setVisibility,
            onFocus,
            onBlur,
            autoFocus,
            openOnFocus,
            setControlRef,
        } = this.props
        const style = { display: 'flex', flexGrow: 1 }
        const dateInputProps = pick(this.props, ['max', 'min'])
        return (
            <div style={style}>
                {Object.keys(value).map((input, i) => (
                    <DateInput
                        key={i}
                        dateFormat={dateFormat}
                        placeholder={placeholder}
                        name={input}
                        disabled={disabled}
                        value={value[input]}
                        onInputChange={onInputChange}
                        setVisibility={setVisibility}
                        onFocus={onFocus}
                        onBlur={onBlur}
                        setControlRef={setControlRef}
                        autoFocus={autoFocus}
                        openOnFocus={openOnFocus}
                        inputClassName={inputClassName}
                        outputFormat={this.props.outputFormat}
                        {...dateInputProps}
                    />
                ))}
            </div>
        )
    }

    /**
   * базовый рендер
   */
    render() {
        const { inputRef } = this.props
        const style = {
            display: 'flex',
            justifyContent: 'space-between',
            flexGrow: 1,
        }
        return (
            <div ref={inputRef} style={style}>
                {this.renderInputs()}
            </div>
        )
    }
}

DateInput.propTypes = {
    dateFormat: PropTypes.string,
    disabled: PropTypes.bool,
    placeholder: PropTypes.oneOfType([PropTypes.string, PropTypes.bool]),
    value: PropTypes.instanceOf(moment),
    onInputChange: PropTypes.func,
    setVisibility: PropTypes.func,
    onFocus: PropTypes.func,
    onBlur: PropTypes.func,
    autoFocus: PropTypes.bool,
    openOnFocus: PropTypes.bool,
    min: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
    max: PropTypes.oneOfType([
        PropTypes.instanceOf(moment),
        PropTypes.instanceOf(Date),
        PropTypes.string,
    ]),
}

DateInput.defaultProps = {
    autoFocus: false,
    openOnFocus: false,
    onFocus: () => {},
    onBlur: () => {},
}

export default DateInputGroup
