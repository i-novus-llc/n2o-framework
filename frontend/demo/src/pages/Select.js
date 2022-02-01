import React from 'react';
import { Container, Row, Col, Button } from 'reactstrap';

import Select from '../components/controls/Select/Select'

class SelectPage extends React.Component {

  render() {
    const props = {
      value: 'brisbane',
      disabled: false,
      options: [
        { label: 'Adelaide', value: 'adelaide' },
        { label: 'Brisbane', value: 'brisbane' },
        { label: 'Canberra', value: 'canberra' },
        { label: 'Darwin', value: 'darwin' },
        { label: 'Hobart', value: 'hobart' },
        { label: 'Melbourne', value: 'melbourne' },
        { label: 'Perth', value: 'perth' },
        { label: 'Sydney', value: 'sydney' },
      ]
    };
    return (
      <Container>
        <Row>
          <Col md={8} mdOffset={3} style={{
            marginTop: '50px',
          }}>
            <Button color="secondary" size="sm" onClick={() => this.props.history.goBack()}>
              <span className="fa fa-chevron-circle-left" /> Назад
            </Button>
            <hr />
            <p className="lead">Пример кастомного выпадающего списка на основе <code>@atlaskit/select</code></p>
            <p><small>Это кастомная страница вне шаблона приложения</small></p>
            <Select { ...props }/>
          </Col>
        </Row>
      </Container>
    )
  }
}

export default SelectPage;
