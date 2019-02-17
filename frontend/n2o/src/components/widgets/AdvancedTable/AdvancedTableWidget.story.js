import React from 'react';
import { pick } from 'lodash';
import { storiesOf } from '@storybook/react';
import { getStubData } from 'N2oStorybook/fetchMock';
import { filterMetadata, newEntry } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';
import metadata from './AdvancedTableWidget.meta';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import withPage from '../../../../.storybook/decorators/withPage';

const stories = storiesOf('Виджеты/Advanced Table', module);

const urlPattern = 'begin:n2o/data';

const tableWidget = pick(metadata['Page_Table'], ['src', 'table', 'dataProvider']);

stories
  .addDecorator(withPage(metadata))
  .add('Метаданные', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        cells: [
          {
            src: 'TextCell',
            id: 'name'
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя',
            width: 200
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия',
            width: 200
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Resize', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        cells: [
          {
            src: 'TextCell',
            id: 'name'
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя',
            width: 200,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия',
            width: 200,
            resizable: true
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ]
      }
    };

    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Checkbox selection', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        cells: [
          {
            src: 'TextCell',
            id: 'name'
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя'
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия'
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ],
        rowSelection: true
      }
    };

    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Column filters', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        cells: [
          {
            src: 'TextCell',
            id: 'name'
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя'
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия'
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Expanded rows', () => {
    fetchMock.restore().get(urlPattern, url => {
      const data = getStubData(url);
      return {
        ...data,
        list: data.list.map(i => ({
          ...i,
          expandedContent: {
            type: 'text',
            value: 'Expanded text'
          }
        }))
      };
    });
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        cells: [
          {
            src: 'TextCell',
            id: 'name'
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя'
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия'
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ],
        expandable: true
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Colspan rowspan', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        cells: [
          {
            src: 'TextCell',
            id: 'name'
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя'
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия'
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Tree view', () => {
    fetchMock.restore().get(urlPattern, url => {
      const data = getStubData(url);
      return {
        ...data,
        list: data.list.map(i => ({
          ...i,
          children: [
            {
              id: i.id + Math.floor(Math.random()) + 123,
              key: i.id + Math.floor(Math.random()) + 123,
              name: 'test',
              children: [
                {
                  id: i.id + Math.floor(Math.random()) + 55,
                  key: i.id + Math.floor(Math.random()) + 55,
                  name: 'more test',
                  children: [
                    {
                      id: i.id + Math.floor(Math.random()) + 11,
                      key: i.id + Math.floor(Math.random()) + 11,
                      name: 'more test'
                    }
                  ]
                }
              ]
            }
          ]
        }))
      };
    });
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        cells: [
          {
            src: 'TextCell',
            id: 'name'
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя'
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия'
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Fixed header', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        cells: [
          {
            src: 'TextCell',
            id: 'name'
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя',
            width: 200
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия',

            width: 200
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ],
        scroll: {
          y: 300
        }
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Fixed columns', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        cells: [
          {
            src: 'TextCell',
            id: 'name'
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя',
            width: 200,
            fixed: 'left'
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия'
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения',
            fixed: 'right',
            width: 200
          }
        ],
        scroll: {
          x: '200%'
        }
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Multi level header', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        bordered: true,
        cells: [
          {
            src: 'TextCell',
            id: 'name'
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            title: 'Имя',
            src: 'TextTableHeader',
            multiHeader: true,
            children: [
              {
                src: 'TextTableHeader',
                id: 'name2',
                title: 'Имя1',
                width: 100,
                dataIndex: 'name',
                key: 'name1'
              },
              {
                src: 'TextTableHeader',
                id: 'name3',
                title: 'Имя2',
                width: 100,
                dataIndex: 'name',
                key: 'name3'
              }
            ]
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия'
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Editable cell', () => {
    fetchMock.restore().get(urlPattern, url => {
      const data = getStubData(url);
      return {
        ...data,
        list: data.list.map(i => ({
          ...i,
          editable: true
        }))
      };
    });
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        cells: [
          {
            src: 'EditableCell',
            id: 'name',
            editable: true,
            control: {
              src: 'InputText'
            }
          },
          {
            src: 'IconCell',
            id: 'surname',
            icon: 'fa fa-plus',
            type: 'iconAndText',
            textPlace: 'right'
          },
          {
            src: 'TextCell',
            id: 'birthday'
          }
        ],
        headers: [
          {
            src: 'TextTableHeader',
            id: 'name',
            sortable: false,
            label: 'Имя',
            width: 200
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия'
          },
          {
            src: 'TextTableHeader',
            id: 'birthday',
            sortable: true,
            label: 'Дата рождения'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('nested', () => {
    fetchMock.restore().get(urlPattern, url => {
      const data = getStubData(url);
      return {
        ...data,
        list: data.list.map(i => ({
          ...i,
          expandedContent: {
            type: 'table',
            columns: [
              {
                title: 'Имя',
                dataIndex: 'name',
                key: 'name'
              },
              {
                title: 'Фамилия',
                dataIndex: 'surname',
                key: 'surname'
              },
              {
                title: 'День рождения',
                dataIndex: 'birthday',
                key: 'birthday'
              }
            ],
            data: [
              {
                name: 'test',
                surname: 'test1',
                birthday: 'test2'
              }
            ]
          }
        }))
      };
    });
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        expandable: true,
        columns: [
          {
            id: 'name',
            title: 'Имя',
            width: 100,
            className: 'name-cell',
            dataIndex: 'name',
            key: 'name',
            headerSrc: 'TextTableHeader',
            cellSrc: 'TextCell'
          },
          {
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            headerSrc: 'TextTableHeader',
            cellSrc: 'IconCell',
            cellOptions: {
              id: 'surname',
              icon: 'fa fa-plus',
              type: 'iconAndText',
              textPlace: 'right'
            }
          },
          {
            id: 'birthday',
            title: 'Дата рождения',
            width: 100,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            headerSrc: 'TextTableHeader',
            cellSrc: 'TextCell'
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  });
