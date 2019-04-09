import React, {Component} from 'react';
import PropTypes from 'prop-types';

import Application from 'n2o/lib/components/core/Application';
import LeftRight from 'n2o/lib/components/layouts/LeftRight/LeftRight';
import Section from 'n2o/lib/components/layouts/Section';
import Tabs from 'n2o/lib/components/regions/Tabs/Tabs';
import Tab from 'n2o/lib/components/regions/Tabs/Tab';
import StandardWidgetLayout from 'n2o/lib/components/layouts/StandardWidgetLayout/StandardWidgetLayout';
import Table from 'n2o/lib/components/widgets/Table/Table';
import TextTableHeader from 'n2o/lib/components/widgets/Table/headers/TextTableHeader';
import TextCell from 'n2o/lib/components/widgets/Table/cells/TextCell';
import { Row, Col } from 'react-bootstrap';
import Input from 'n2o/lib/components/controls/Input/Input'
import StandardField from 'n2o/lib/components/controls/Field/StandardField';
import Form from 'n2o/lib/components/widgets/Form/Form';
import Fieldset from 'n2o/lib/components/widgets/Form/Fieldset';

import PuperWidget from '../widget/PuperWidget';
import SuperInput from '../control/SuperInput';

class TestPage extends Component {
  constructor(props) {
    super(props);
    this.tableData = [
      {id: "1", name: "Foo", surname: "Bar", birthday: "01.01.2001"},
      {id: "2", name: "X", surname: "Y", birthday: "01.01.1001"},
      {id: "3", name: "Test", surname: "Tset", birthday: "01.01.0001"},
    ]
  }

  render() {
    return (
      <Application>
        <LeftRight>
          <Section place="left">
            <div className="alert alert-info">
              <h2>Это тестовая страница N2O</h2>
              <h3>Демонстрирует возможность расширения роутинга</h3>
              <h4>Страница написано полностью на JSX</h4>
            </div>
          </Section>
          <Section place="right">
            <h3>Стандартная связка регион+виджет+контрол</h3>
            <h4>Таблица</h4>
            <StandardWidgetLayout>
              <Section place="center">
                <Table>
                  <Table.Header>
                    <Table.Row>
                      <Table.Cell as="th" component={TextTableHeader} id="name" sortable={false} label="Имя" />
                      <Table.Cell as="th" component={TextTableHeader} id="surname" sortable={false} label="Фамилия" />
                      <Table.Cell as="th" component={TextTableHeader} id="birthday" sortable={false} label="Дата рождения" />
                    </Table.Row>
                  </Table.Header>
                  <Table.Body>
                    {
                      this.tableData.map((data) => (
                        <Table.Row>
                          <Table.Cell component={TextCell} model={data} id="name" fieldKey="name" />
                          <Table.Cell id="surname">
                            <TextCell model={data} fieldKey="surname" />
                          </Table.Cell>
                          <Table.Cell component={TextCell} model={data} id="birthday" fieldKey="birthday" />
                        </Table.Row>
                      ))
                    }
                  </Table.Body>
                </Table>
              </Section>
            </StandardWidgetLayout>
            <h4>Форма</h4>
            <StandardWidgetLayout>
              <Section place="center">
                <Form autoFocus={true}>
                  <Fieldset>
                    <Row>
                      <Col md={6}>
                        <Form.Field component={StandardField} control = {Input} name='name' label='Имя' description='Введите имя'/>
                      </Col>
                      <Col md={6}>
                        <Form.Field component={StandardField} control = {Input} name='lastname' label='Фамилия' description='Введите фамилию'/>
                      </Col>
                    </Row>
                  </Fieldset>
                </Form>
              </Section>
            </StandardWidgetLayout>
            <h3>Пользовательская связка регион+виджет+контрол</h3>
            <PuperWidget />
            <SuperInput />
          </Section>
        </LeftRight>
      </Application>
    );
  }
}

TestPage.propTypes = {};
TestPage.defaultProps = {};

export default TestPage;