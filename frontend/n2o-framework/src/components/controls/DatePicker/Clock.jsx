import React from 'react'
import moment from 'moment'
import PropTypes from 'prop-types'

/**
 * Компонент Clock
 * @reactProps {function} onClick - событие клика
 */
class Clock extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            time: moment(),
        }
    }

    componentDidMount() {
        setInterval(() => {
            this.setState({ time: moment() })
        })
    }

    render() {
        return (
            <div className="n2o-calendar-clock" onClick={this.props.onClick}>
                {this.state.time.format('H:mm:ss')}
            </div>
        )
    }
}

Clock.propTypes = {
    onClick: PropTypes.func,
}

export default Clock
