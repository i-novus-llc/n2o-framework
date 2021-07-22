import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import map from 'lodash/map'
import { withResizeDetector } from 'react-resize-detector'

import calcCols from './utils'
import TilesCell from './TilesCell'

/**
 * Tiles
 * @reactProps {string} className - имя css класса карточки
 * @reactProps {array} tiles - массив объектов cell из которых состоит виджет
 * @reactProps {array} data - данные объектов cell
 * @reactProps {number} colsSm - количество колонок в мобильном режиме
 * @reactProps {number} colsMd - количество колонок в режиме планшета
 * @reactProps {number} colsLg - количество колонок в режиме десктопа
 * @reactProps {number} id - id виджета
 * @reactProps {string} tileWidth - ширина ввиджета
 * @reactProps {string} tileHeight - высота виджета
 */

function Tiles(props) {
    const {
        tile,
        className,
        data,
        id,
        colsSm,
        colsMd,
        colsLg,
        width,
        tileWidth,
        tileHeight,
        onResolve,
        dispatch,
    } = props
    const col = calcCols(colsSm, colsMd, colsLg, width)

    const renderTilesItem = (element, index) => (
        <div className={`col-${col} d-flex justify-content-center`}>
            <div
                className={classNames('n2o-tiles__item')}
                style={{ width: tileWidth, minHeight: tileHeight }}
            >
                {map(tile, (cell) => {
                    const { component } = cell

                    return (
                        <TilesCell
                            className={classNames('n2o-tiles__cell', component.className)}
                            index={index}
                            widgetId={id}
                            model={element}
                            onResolve={onResolve}
                            dispatch={dispatch}
                            {...cell}
                        />
                    )
                })}
            </div>
        </div>
    )

    return (
        <div className={classNames('n2o-tiles__container col-12', className)}>
            {
                data?.length
                    ? map(data, (element, index) => renderTilesItem(element, index))
                    : ''
            }
        </div>
    )
}

Tiles.propTypes = {
    /**
     * имя css класса карточки
     */
    className: PropTypes.string,
    /**
     * данные объектов cell
     */
    data: PropTypes.array,
    /**
     *  количество колонок в мобильном режиме
     */
    colsSm: PropTypes.number,
    /**
     * количество колонок в режиме планшета
     */
    colsMd: PropTypes.number,
    /**
     * количество колонок в режиме десктопа
     */
    colsLg: PropTypes.number,
    /**
     * id виджета
     */
    id: PropTypes.number,
    /**
     * высота виджета
     */
    tileHeight: PropTypes.string,
    /**
     * ширина виджета
     */
    tileWidth: PropTypes.string,
    tile: PropTypes.node,
    width: PropTypes.number,
    onResolve: PropTypes.func,
    dispatch: PropTypes.func,
}

Tiles.defaultProps = {
    tileWidth: '260px',
    tileHeight: '350px',
}

export { Tiles }

export default withResizeDetector(Tiles)
