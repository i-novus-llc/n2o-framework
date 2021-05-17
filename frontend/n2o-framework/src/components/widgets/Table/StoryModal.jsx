import React from 'react'
import PropTypes from 'prop-types'
import Row from 'reactstrap/lib/Row'
import Col from 'reactstrap/lib/Col'

import controls from '../../controls'
import ReduxForm from '../Form/ReduxForm'
import Fieldset from '../Form/Fieldset'

export const exportFormName = 'storyForm'

/**
 * Компонент StoryModal
 * @reactProps {object} type
 * @reactProps {any} code
 */
// eslint-disable-next-line react/prefer-stateless-function
class StoryModal extends React.Component {
    render() {
        const dateControl = { component: controls.DatePicker }
        const inputControl = { component: controls.InputText }

        return (
            <ReduxForm form={exportFormName}>
                <Fieldset>
                    <Row>
                        <Col md={12}>
                            <ReduxForm.Field control={inputControl} name="name" label="Имя" />
                        </Col>
                    </Row>
                    <Row>
                        <Col md={12}>
                            <ReduxForm.Field
                                control={inputControl}
                                name="surname"
                                label="Фамилия"
                            />
                        </Col>
                    </Row>
                    <Row>
                        <Col md={12}>
                            <ReduxForm.Field
                                control={dateControl}
                                name="birthday"
                                label="Дата рождения"
                            />
                        </Col>
                    </Row>
                </Fieldset>
            </ReduxForm>
        )
    }
}

StoryModal.propTypes = {
    // eslint-disable-next-line react/no-unused-prop-types
    type: PropTypes.object,
    // eslint-disable-next-line react/no-unused-prop-types
    code: PropTypes.string,
}

export default StoryModal
