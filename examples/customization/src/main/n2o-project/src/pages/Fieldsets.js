import React from 'react';
import Application from 'n2o/lib/components/core/Application';
import { Row, Col } from 'reactstrap';
import Single from 'n2o/lib/components/layouts/Single/Single';
import Section from 'n2o/lib/components/layouts/Section';

class Fieldsets extends React.Component {
    render() {
        return (
            <Application>
                <Single>
                    <Section place={'single'}>
                        <Row>
                            <Col>
                                <h2></h2>

                            </Col>
                        </Row>
                    </Section>
                </Single>
            </Application>
        );
    }
}

export default Fieldsets;