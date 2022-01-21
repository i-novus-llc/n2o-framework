import React from 'react'
import PropTypes from 'prop-types'
import { Form as BaseForm, FormGroup, Label, Row, Col } from 'reactstrap'

import { mapToNumOrStr } from '../../utils'

function Form({ rows, cols }) {
    const renderCols = () => (
        <Col>
            <FormGroup>
                <Label>
                    <div className="n2o-placeholder-content form-label mb-0" />
                </Label>
                <div className="n2o-placeholder-content mb-0" />
            </FormGroup>
        </Col>
    )

    const renderListItem = () => (
        <Row form>{mapToNumOrStr(cols, renderCols)}</Row>
    )

    return <BaseForm>{mapToNumOrStr(rows, renderListItem)}</BaseForm>
}

Form.propTypes = {
    /**
   * Количество строк
   */
    rows: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    /**
   * Количество столбцов
   */
    cols: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
}

Form.defaultProps = {
    rows: 2,
    cols: 1,
}

export default Form
