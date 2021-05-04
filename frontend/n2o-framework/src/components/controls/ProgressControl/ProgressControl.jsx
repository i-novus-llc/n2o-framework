import React from 'react'
import PropTypes from 'prop-types'
import Progress from 'reactstrap/lib/Progress'
import isUndefined from 'lodash/isUndefined'
import isArray from 'lodash/isArray'
import map from 'lodash/map'

function ProgressControl(props) {
    const { multi, barClassName, bars, value, barText, max } = props

    const renderProgressControl = () => {
        const multiProgressControl = multi && !isUndefined(bars) && isArray(bars)
        const barsCollection = multiProgressControl ? bars : []

        // map ProgressControls из bars если multi
        const mapProgressControls = () => map(barsCollection, (bar) => {
            const { id, barText } = bar

            return (
                <div className="n2o-progress-control">
                    <Progress
                        bar
                        key={id}
                        className={barClassName}
                        max={max}
                        value={!isUndefined(value) && value[id]}
                        {...bar}
                    >
                        {barText}
                    </Progress>
                </div>
            )
        })

        // мультирежим ProgressControl
        const renderMultiProgressControl = () => (
            <div className="n2o-progress-control">
                <Progress multi>{mapProgressControls()}</Progress>
            </div>
        )

        // одиночный режим ProgressControl
        const renderSimpleProgressControl = () => (
            <div className="n2o-progress-control">
                <Progress {...props}>{barText}</Progress>
            </div>
        )

        // multi или одиночный
        const progressControlSelection = () => (multiProgressControl
            ? renderMultiProgressControl()
            : renderSimpleProgressControl())

        return progressControlSelection()
    }

    return renderProgressControl()
}

ProgressControl.propTypes = {
    /**
     * флаг мультирежима в Progress
     */
    multi: PropTypes.bool.isRequired,
    /**
     * Массив с Progress-bars в мультирежиме
     */
    bars: PropTypes.array.isRequired,
    /**
     * id Progress
     */
    id: PropTypes.string.isRequired,
    /**
     * Флаг анимированности
     */
    animated: PropTypes.bool,
    /**
     * Флаг штриховки
     */
    striped: PropTypes.bool,
    /**
     * class контейнера
     */
    className: PropTypes.string.isRequired,
    /**
     * class progress-bar
     */
    barClassName: PropTypes.string.isRequired,
    /**
     * title на Progress
     */
    barText: PropTypes.string.isRequired,
    /**
     * значение, на сколько заполнен Progress
     */
    value: PropTypes.number,
    /**
     * максиальное значение шкалы
     */
    max: PropTypes.number,
    /**
     * цвет шкалы Progress (success, info, danger, warning)
     */
    color: PropTypes.string,
}

ProgressControl.defaultProps = {
    multi: false,
    animated: false,
    striped: false,
}

export default ProgressControl
