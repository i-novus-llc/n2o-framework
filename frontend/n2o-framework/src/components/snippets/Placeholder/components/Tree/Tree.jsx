import React from 'react'
import PropTypes from 'prop-types'

import { mapToNumOrStr } from '../../utils'

function Tree({ rows, chevron }) {
    const renderLi = () => (
        <li>
            {chevron && <i className="fa fa-angle-right" />}
            <div className="n2o-placeholder-content" />
        </li>
    )

    return (
        <ul className="n2o-placeholder-tree">{mapToNumOrStr(rows, renderLi)}</ul>
    )
}

Tree.propTypes = {
    /**
   * Количество строк
   */
    rows: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
   * Флаг включения отрисовки иконки chevron рядом со строкой
   */
    chevron: PropTypes.bool,
}

Tree.defaultProps = {
    rows: 2,
    chevron: false,
}

export default Tree
