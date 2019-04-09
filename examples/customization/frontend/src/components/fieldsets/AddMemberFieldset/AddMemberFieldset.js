import React from 'react';
import { isEqual, map } from 'lodash';
import { Row, Col, Button } from 'reactstrap';
import ReduxField from 'n2o/lib/components/widgets/Form/ReduxField';
import StandardField from 'n2o/lib/components/widgets/Form/fields/StandardField/StandardField';
import PropTypes from 'prop-types';

class AddMemberFieldset extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            rows: props.rows
        };

        this.addMember = this.addMember.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (!isEqual(prevProps.rows, this.props.rows)) {
            this.setState({ rows: this.props.rows });
        }
    }

    addMember() {
        const rows = this.state.rows;
        const template = {
            "cols": [
                {
                    "fields": [
                        {
                            ...this.context.resolveProps({
                                "id": "surname" + rows.length,
                                "src": "StandardField",
                                "label": "Фамилия",
                                "dependency": [],
                                "control": {
                                    "readOnly": false,
                                    "type": "text",
                                    "disabled": false,
                                    "src": "InputText"
                                }
                            }, StandardField),
                        }
                    ]
                },
                {
                    "fields": [
                        {
                            ...this.context.resolveProps({
                                "id": "name" + rows.length,
                                "src": "StandardField",
                                "label": "Имя",
                                "dependency": [],
                                "control": {
                                    "readOnly": false,
                                    "type": "text",
                                    "disabled": false,
                                    "src": "InputText"
                                }
                            }, StandardField),
                        }
                    ]
                }
            ]
        };
        rows.push(template);
        this.setState({
            rows
        });
    }

    renderFields(rows) {
        return map(rows, ({ cols }) => (
            <Row>
                {map(cols, ({ fields }) => (
                    <Col md={6}>
                        {map(fields, item => <ReduxField {...item} />)}
                    </Col>
                ))}
            </Row>
        ));
    }

    render() {
        return (
            <div style={{
                padding: '40px'
            }}>
                <h1>Пример AddMemberFieldset</h1>
                <div>
                    <div style={{
                        display: 'flex',
                    }}>
                        <div style={{ flexGrow: 1 }}>
                            {this.renderFields(this.state.rows)}
                        </div>
                        <div style={{ alignSelf: 'flex-end', marginLeft: 20, marginBottom: 25 }}>
                            <Button  onClick={this.addMember} size={'sm'} style={{ width: 38, height: 38 }}>
                                <i className="fa fa-plus-circle" />
                            </Button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

AddMemberFieldset.contextTypes = {
    resolveProps: PropTypes.func
};

export default AddMemberFieldset;