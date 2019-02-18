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
import { page } from 'N2oStorybook/fetchMock';
import AdvancedTable from './AdvancedTable';

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
  .add('Resizable колонки', () => {
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
  .add('Выбор строк чекбоксом', () => {
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
  .add('Фильтр в заголовках', () => {
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
            filterable: true
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
  .add('Контент в подстроке', () => {
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
    fetchMock.restore().get(urlPattern, url => {
      const data = getStubData(url);

      return {
        ...data,
        list: data.list.map((i, index) => {
          let rowSpan = 1;
          let colSpan = 1;
          if (index === 0) {
            rowSpan = 2;
          }
          if (index === 1) {
            rowSpan = 0;
          }

          return {
            ...i,
            span: {
              rowSpan,
              colSpan
            }
          };
        })
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
            textPlace: 'right',
            span: {
              rowSpan: 2,
              colSpan: 2
            },
            hasSpan: true
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
            colSpan: 2
          },
          {
            src: 'TextTableHeader',
            id: 'surname',
            sortable: true,
            label: 'Фамилия',
            colSpan: 0
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
  .add('Вид дерево', () => {
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
  .add('Зафиксированный заголовок', () => {
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
  .add('Зафиксированные колонки', () => {
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
  .add('Многоуровневый заголовок', () => {
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
            src: 'TextTableHeader',
            title: 'Имя',
            id: 'name',
            key: 'name',
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
  .add('Редактируемая ячейка', () => {
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
  .add('Подтаблица', () => {
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
  })
  .add('Экшен AdvancedTable', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    fetchMock.get('begin:n2o/page', page);
    const props = {
      ...tableWidget,
      table: {
        ...tableWidget.table,
        hasSelect: false,
        hasFocus: false,
        rowClick: {
          src: 'perform',
          options: {
            type: 'n2o/modals/INSERT',
            payload: {
              pageUrl: '/Uid',
              size: 'sm',
              visible: true,
              closeButton: true,
              title: 'Новое модальное окно',
              pageId: 'Uid'
            }
          }
        },
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
  .add('Компонент со всеми фичами', () => {
    const columns = [
      {
        title: 'Имя',
        id: 'name',
        multiHeader: true,
        children: [
          {
            title: 'Имя1',
            id: 'name1',
            dataIndex: 'name',
            width: 100
          },
          {
            title: 'Имя2',
            id: 'name2',
            dataIndex: 'name',
            width: 100
          }
        ]
      },
      {
        title: 'Фамилия',
        id: 'surname',
        dataIndex: 'surname',
        resizable: true,
        width: 100,
        filterable: true
      },
      {
        title: 'Описание',
        id: 'description',
        dataIndex: 'description'
      }
    ];

    const data = [
      {
        id: 1,
        name: 'Name 1',
        surname: 'Surname 1',
        description: 'Текст в подстроке',
        expandedContent: {
          type: 'text',
          value: 'Expanded text'
        }
      },
      {
        id: 2,
        name: 'Name 2',
        surname: 'Surname 2',
        description: 'HTML в подстроке',
        expandable: true,
        expandedContent: {
          type: 'html',
          value: '<h1>Заголовок<h1/>'
        }
      },
      {
        id: 3,
        name: 'Name 3',
        surname: 'Surname 3',
        description: 'Таблица в подстроке',
        expandable: true,
        expandedContent: {
          type: 'table',
          columns: [
            {
              title: 'Sub title 1',
              dataIndex: 'subTitle1'
            }
          ],
          data: [
            {
              subTitle1: 'sub value 1'
            }
          ]
        }
      }
    ];

    return (
      <AdvancedTable
        rowClick={true}
        expandable={true}
        rowSelection={true}
        bordered={true}
        columns={columns}
        data={data}
      />
    );
  });
