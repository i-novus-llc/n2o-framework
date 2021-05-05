import React from 'react'
import PropTypes from 'prop-types'
import ListGroup from 'reactstrap/lib/ListGroup'
import ListGroupItem from 'reactstrap/lib/ListGroupItem'

import { mapToNumOrStr } from '../../utils'

function List({ paragraphs, rows, avatar }) {
    const renderParagraphs = () => (
        <div className="n2o-placeholder-content item" />
    )

    const renderAvatar = <div className="n2o-placeholder-content avatar" />

    const renderListItem = () => (
        <ListGroupItem className="n2o-placeholder-list">
            {avatar && renderAvatar}
            {mapToNumOrStr(paragraphs, renderParagraphs)}
        </ListGroupItem>
    )

    return <ListGroup>{mapToNumOrStr(rows, renderListItem)}</ListGroup>
}

List.propTypes = {
    /**
   * Количество строк
   */
    rows: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
   * Количество абзацев
   */
    paragraphs: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
   * Флаг включения аватара
   */
    avatar: PropTypes.bool,
}

List.defaultProps = {
    rows: 1,
    paragraphs: 2,
    avatar: false,
}

export default List
