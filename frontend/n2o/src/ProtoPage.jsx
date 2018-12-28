import React from 'react';
import {
  ButtonToolbar,
  UncontrolledDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  Row,
  Col
} from 'reactstrap';

import Application from './components/core/Application';
import LeftRight from './components/layouts/LeftRight/LeftRight';
import Section from './components/layouts/Section';
import Tabs from './components/regions/Tabs/Tabs';
import Tab from './components/regions/Tabs/Tab';
import List from './components/regions/List/List';
import ListItem from './components/regions/List/ListItem';
import StandardWidgetLayout from './components/layouts/StandardWidgetLayout/StandardWidgetLayout';
import Alerts from './components/snippets/Alerts/Alerts';
import TableContainer from './components/widgets/Table/TableContainer';

import Table from './components/widgets/Table/Table';
import TextTableHeader from './components/widgets/Table/headers/TextTableHeader';
import TextCell from './components/widgets/Table/cells/TextCell/TextCell';

import Input from 'components/controls/Input/Input';
import StandardField from 'components/controls/Field/StandardField';
import Form from 'components/widgets/Form/Form';
import ReduxForm from 'components/widgets/Form/ReduxForm';
import Fieldset from 'components/widgets/Form/Fieldset';
import FormContainer from 'components/widgets/Form/FormContainer';
import Actions from './components/actions/Actions';

import DatePicker from './components/controls/DatePicker/DatePicker';
import DateInterval from './components/controls/DatePicker/DateInterval';

class ProtoPage extends React.Component {
  constructor(props) {
    super(props);
    this.tableData = [
      { id: '1', name: 'Foo', surname: 'Bar', birthday: '01.01.2001' },
      { id: '2', name: 'X', surname: 'Y', birthday: '01.01.1001' },
      { id: '3', name: 'Test', surname: 'Tset', birthday: '01.01.0001' }
    ];
  }

  getTableContainerProps() {
    return {
      widgetId: 'ProtoPage.patients',
      size: '10',
      fetchOnInit: true,
      hasSelect: true,
      headers: [
        { id: 'name', component: TextTableHeader, sortable: false, label: 'Имя' },
        { id: 'surname', component: TextTableHeader, sortable: false, label: 'Фамилия' },
        {
          id: 'birthday',
          component: TextTableHeader,
          sortable: false,
          sorting: 'ASC',
          label: 'Дата рождения'
        }
      ],
      cells: [
        { id: 'name', component: TextCell, fieldKey: 'name' },
        { id: 'surname', component: TextCell, fieldKey: 'surname' },
        { id: 'birthday', component: TextCell, fieldKey: 'birthday' }
      ]
    };
  }

  getFormContainerProps() {
    return {
      widgetId: 'ProtoPage_Form',
      autoFocus: true,
      fieldsets: [
        {
          src: 'StandardFieldset',
          rows: [
            {
              cols: [
                {
                  size: 6,
                  fields: [
                    {
                      src: StandardField,
                      id: 'surname',
                      label: 'Фамилия',
                      name: 'surname',
                      control: Input
                    }
                  ]
                },
                {
                  size: 6,
                  fields: [
                    {
                      src: StandardField,
                      id: 'name',
                      label: 'Имя',
                      control: Input
                    }
                  ]
                }
              ]
            },
            {
              cols: [
                {
                  size: 6,
                  fields: [
                    {
                      src: StandardField,
                      id: 'date',
                      label: 'Дата',
                      control: DatePicker,
                      dateFormat: 'DD.MM.YYYY',
                      defaultValue: '11.11.1111',
                      popupPlacement: 'right'
                    }
                  ]
                },
                {
                  size: 6,
                  fields: [
                    {
                      src: StandardField,
                      id: 'date-interval',
                      label: 'Интервал даты',
                      control: DateInterval,
                      dateFormat: 'DD.MM.YYYY',
                      defaultValue: ['11.11.1111', '12.12.1212']
                    }
                  ]
                }
              ]
            },
            {
              cols: [
                {
                  size: 6,
                  fields: [
                    {
                      src: StandardField,
                      id: 'date',
                      label: 'Дата',
                      control: DatePicker,
                      dateFormat: 'DD.MM.YYYY',
                      defaultValue: '11.11.1111',
                      popupPlacement: 'auto'
                    }
                  ]
                },
                {
                  size: 6,
                  fields: [
                    {
                      src: StandardField,
                      id: 'date-interval',
                      label: 'Интервал даты',
                      control: DateInterval,
                      dateFormat: 'DD.MM.YYYY',
                      defaultValue: ['11.11.1111', '12.12.1212']
                    }
                  ]
                }
              ]
            }
          ]
        }
      ]
    };
  }

  render() {
    return (
      <Application>
        <LeftRight>
          <Section place="left">
            <Tabs>
              <Tab id="one" title="Таблица через JSX">
                <StandardWidgetLayout>
                  <Section place="top">
                    <Alerts widgetId="ProtoPage.patients" />
                  </Section>
                  <Section place="topLeft">
                    <Actions
                      toolbar={[
                        {
                          buttons: [
                            {
                              id: 'testBTN',
                              title: 'Изменить',
                              hint: 'hint',
                              confirm: {
                                size: 'lg',
                                closeButton: true
                              },
                              actionId: 'invoke'
                            },
                            {
                              id: 'testBTN1',
                              title: 'Изменить1',
                              hint: 'hint1',
                              subMenu: [
                                {
                                  id: 'testBTN2',
                                  title: 'Изменить2',
                                  hint: 'hint1',
                                  actionId: 'invoke',
                                  confirm: {
                                    size: 'lg',
                                    closeButton: true
                                  }
                                }
                              ]
                            }
                          ]
                        }
                      ]}
                      actions={{
                        invoke: { src: 'invoke', options: { modelLink: 'models.resolveModel' } }
                      }}
                    />
                  </Section>
                  <Section place="center">
                    <Table>
                      <Table.Header>
                        <Table.Row>
                          <Table.Cell
                            as="th"
                            component={TextTableHeader}
                            id="name"
                            sortable={false}
                            label="Имя"
                          />
                          <Table.Cell
                            as="th"
                            component={TextTableHeader}
                            id="surname"
                            sortable={false}
                            label="Фамилия"
                          />
                          <Table.Cell
                            as="th"
                            component={TextTableHeader}
                            id="birthday"
                            sortable={false}
                            label="Дата рождения"
                          />
                        </Table.Row>
                      </Table.Header>
                      <Table.Body>
                        {this.tableData.map(data => (
                          <Table.Row>
                            <Table.Cell
                              component={TextCell}
                              model={data}
                              id="name"
                              fieldKey="name"
                            />
                            <Table.Cell id="surname">
                              <TextCell model={data} fieldKey="surname" />
                            </Table.Cell>
                            <Table.Cell
                              component={TextCell}
                              model={data}
                              id="birthday"
                              fieldKey="birthday"
                            />
                          </Table.Row>
                        ))}
                      </Table.Body>
                    </Table>
                  </Section>
                </StandardWidgetLayout>
              </Tab>
              <Tab id="two" title="Таблица через TableContainer" active>
                <StandardWidgetLayout>
                  <Section place="top">
                    <Alerts widgetId="ProtoPage.patients" />
                  </Section>
                  <Section place="topLeft">
                    <Actions
                      toolbar={[
                        { buttons: [{ title: 'Изменить', actionId: 'dummy' }] },
                        { buttons: [{ title: 'Удалить', actionId: 'dummy' }] }
                      ]}
                      actions={{ dummy: { src: 'dummyImpl' } }}
                    />
                  </Section>
                  <Section place="center">
                    <TableContainer {...this.getTableContainerProps()} />
                  </Section>
                </StandardWidgetLayout>
              </Tab>
              <Tab id="three" title="Форма через JSX">
                <StandardWidgetLayout>
                  <Section place="top">
                    <Alerts widgetId="ProtoPage.patients" />
                  </Section>
                  <Section place="topLeft">
                    <Actions
                      toolbar={[
                        { buttons: [{ title: 'Изменить', actionId: 'dummy' }] },
                        { buttons: [{ title: 'Удалить', actionId: 'dummy' }] }
                      ]}
                      actions={{ dummy: { src: 'dummyImpl' } }}
                    />
                  </Section>
                  <Section place="center">
                    <Form autoFocus={true}>
                      <Fieldset>
                        <Row>
                          <Col md={6}>
                            <Form.Field
                              component={StandardField}
                              control={Input}
                              name="name"
                              label="Имя"
                              description="Введите имя"
                            />
                          </Col>
                          <Col md={6}>
                            <Form.Field
                              component={StandardField}
                              control={Input}
                              name="lastname"
                              label="Фамилия"
                              description="Введите фамилию"
                            />
                          </Col>
                        </Row>
                        <Row>
                          <Col md={6}>
                            <Form.Field
                              component={StandardField}
                              control={DatePicker}
                              name="date"
                              label="Дата"
                            />
                          </Col>
                          <Col md={6}>
                            <Form.Field
                              component={StandardField}
                              control={DateInterval}
                              name="date-interval"
                              label="Итервал даты"
                            />
                          </Col>
                        </Row>
                      </Fieldset>
                    </Form>
                  </Section>
                </StandardWidgetLayout>
              </Tab>
              <Tab id="four" title="Redux-форма через JSX">
                <StandardWidgetLayout>
                  <Section place="top">
                    <Alerts widgetId="ProtoPage.patients" />
                  </Section>
                  <Section place="topLeft">
                    <Actions
                      toolbar={[
                        { buttons: [{ title: 'Изменить', actionId: 'dummy' }] },
                        { buttons: [{ title: 'Удалить', actionId: 'dummy' }] }
                      ]}
                      actions={{ dummy: { src: 'dummyImpl' } }}
                    />
                  </Section>
                  <Section place="center">
                    <ReduxForm
                      autoFocus={true}
                      form="ProtoPage.ReduxForm"
                      datasource={[{ name: 'name', value: 'Ivan' }]}
                    >
                      <Fieldset>
                        <Row>
                          <Col md={6}>
                            <ReduxForm.Field
                              component={StandardField}
                              control={Input}
                              name="name"
                              label="Имя"
                              description="Введите имя"
                              labelStyle={{ color: 'red' }}
                            />
                          </Col>
                          <Col md={6}>
                            <ReduxForm.Field
                              component={StandardField}
                              control={Input}
                              name="lastname"
                              label="Фамилия"
                              description="Введите фамилию"
                              controlClass={'test'}
                            />
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <ButtonToolbar>
                              <UncontrolledDropdown>
                                <DropdownToggle caret size="lg" id="dropdown-size-large">
                                  Large button
                                </DropdownToggle>
                                <DropdownMenu>
                                  <DropdownItem eventKey="1">Action</DropdownItem>
                                  <DropdownItem eventKey="2">Another action</DropdownItem>
                                  <DropdownItem eventKey="3">Something else here</DropdownItem>
                                  <DropdownItem divider />
                                  <DropdownItem eventKey="4">Separated link</DropdownItem>
                                </DropdownMenu>
                              </UncontrolledDropdown>
                            </ButtonToolbar>
                          </Col>
                        </Row>
                        <Row>
                          <Col>
                            <ButtonToolbar>
                              <UncontrolledDropdown>
                                <DropdownToggle caret size="lg" id="dropdown-size-large">
                                  Large button
                                </DropdownToggle>
                                <DropdownMenu>
                                  <DropdownItem eventKey="1">Action</DropdownItem>
                                  <DropdownItem eventKey="2">Another action</DropdownItem>
                                  <DropdownItem eventKey="3">Something else here</DropdownItem>
                                  <DropdownItem divider />
                                  <DropdownItem eventKey="4">Separated link</DropdownItem>
                                </DropdownMenu>
                              </UncontrolledDropdown>
                            </ButtonToolbar>
                          </Col>
                        </Row>
                        <Row>
                          <Col md={6}>
                            <ReduxForm.Field
                              component={StandardField}
                              control={DatePicker}
                              name="date"
                              label="Дата"
                            />
                          </Col>
                          <Col md={6}>
                            <ReduxForm.Field
                              component={StandardField}
                              control={DateInterval}
                              name="date-interval"
                              label="Итервал даты"
                            />
                          </Col>
                        </Row>
                      </Fieldset>
                    </ReduxForm>
                  </Section>
                </StandardWidgetLayout>
              </Tab>
              <Tab id="five" title="Таблица через FormContainer" active>
                <StandardWidgetLayout>
                  <Section place="top">
                    <Alerts widgetId="ProtoPage.patients" />
                  </Section>
                  <Section place="topLeft">
                    <Actions
                      toolbar={[
                        { buttons: [{ title: 'Изменить', actionId: 'dummy' }] },
                        { buttons: [{ title: 'Удалить', actionId: 'dummy' }] }
                      ]}
                      actions={{ dummy: { src: 'dummyImpl' } }}
                    />
                  </Section>
                  <Section place="center">
                    <FormContainer {...this.getFormContainerProps()} />
                  </Section>
                </StandardWidgetLayout>
              </Tab>
            </Tabs>
          </Section>
          <Section place="right">
            <List>
              <ListItem id="one" title="Первый" active>
                Лист 1
              </ListItem>
              <ListItem id="two" title="Второй">
                Лист 2
              </ListItem>
              <ListItem id="three" title="Третий" active>
                Лист 3
              </ListItem>
              <ListItem id="four" title="Четвертый">
                Лист 4
              </ListItem>
            </List>
          </Section>
        </LeftRight>
      </Application>
    );
  }
}

export default ProtoPage;
