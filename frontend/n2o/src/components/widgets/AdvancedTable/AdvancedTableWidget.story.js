import React from 'react';
import { storiesOf } from '@storybook/react';
import { getStubData } from 'N2oStorybook/fetchMock';
import { filterMetadata, newEntry } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';
import { set } from 'lodash';
import metadata from './AdvancedTableWidget.meta';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import { START_INVOKE } from '../../../constants/actionImpls';
import { omit } from 'lodash';
import { id } from '../../../utils/id';
import cloneObject from '../../../utils/cloneObject';
import AuthButtonContainer from '../../../core/auth/AuthLogin';
import withPage from '../../../../.storybook/decorators/withPage';
import AdvancedTable from './AdvancedTableContainer';

const stories = storiesOf('Виджеты/Advanced Table', module);

const urlPattern = 'begin:n2o/data';

stories
  .addDecorator(withPage(metadata))
  .add('test', () => {
    return (
      <AdvancedTable
        columns={[
          {
            title: 'test',
            key: 'test',
            dataIndex: 'test',
            width: 100,
            resizable: true
          },
          {
            title: 'test1',
            key: 'test1',
            dataIndex: 'test1',
            width: 100,
            resizable: true
          }
        ]}
      />
    );
  })
  .add('Метаданные', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        isActive: true,
        columns: [
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'name',
            title: 'Имя',
            width: 100,
            dataIndex: 'name',
            key: 'name',
            className: 'name-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'IconCell',
              id: 'surname',
              icon: 'fa fa-plus',
              type: 'iconAndText',
              textPlace: 'right'
            },
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'birthday',
            title: 'Дата рождения',
            width: 100,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            sortable: true
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Resize', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        columns: [
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'name',
            title: 'Имя',
            width: 100,
            className: 'name-cell',
            dataIndex: 'name',
            key: 'name',
            resizable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            className: 'surname-cell',
            dataIndex: 'surname',
            key: 'surname',
            resizable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'birthday',
            title: 'Дата рождения',
            width: 100,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            resizable: true
          }
        ]
      }
    };

    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Checkbox selection', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        columns: [
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'name',
            title: 'Имя',
            width: 100,
            dataIndex: 'name',
            key: 'name',
            className: 'name-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'IconCell',
              id: 'surname',
              icon: 'fa fa-plus',
              type: 'iconAndText',
              textPlace: 'right'
            },
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'birthday',
            title: 'Дата рождения',
            width: 100,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            sortable: true
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
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        columns: [
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'name',
            title: 'Имя',
            width: 100,
            className: 'name-cell',
            dataIndex: 'name',
            key: 'name',
            resizable: true,
            filterable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            resizable: true,
            filterable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'birthday',
            title: 'Дата рождения',
            width: 100,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            resizable: true
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
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        columns: [
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'name',
            title: 'Имя',
            width: 100,
            dataIndex: 'name',
            key: 'name',
            className: 'name-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'IconCell',
              id: 'surname',
              icon: 'fa fa-plus',
              type: 'iconAndText',
              textPlace: 'right'
            },
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'birthday',
            title: 'Дата рождения',
            width: 100,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            sortable: true
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
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        columns: [
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'name',
            title: 'Имя',
            width: 100,
            dataIndex: 'name',
            key: 'name',
            className: 'name-cell',
            sortable: true,
            colSpan: 2
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'IconCell',
              id: 'surname',
              icon: 'fa fa-plus',
              type: 'iconAndText',
              textPlace: 'right'
            },
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            sortable: true,
            colSpan: 0
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'birthday',
            title: 'Дата рождения',
            width: 100,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            sortable: true
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
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        columns: [
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'name',
            title: 'Имя',
            width: 100,
            dataIndex: 'name',
            key: 'name',
            className: 'name-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'IconCell',
              id: 'surname',
              icon: 'fa fa-plus',
              type: 'iconAndText',
              textPlace: 'right'
            },
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'birthday',
            title: 'Дата рождения',
            width: 100,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            sortable: true
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })

  .add('Fixed header', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        columns: [
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'name',
            title: 'Имя',
            width: 100,
            dataIndex: 'name',
            key: 'name',
            className: 'name-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'IconCell',
              id: 'surname',
              icon: 'fa fa-plus',
              type: 'iconAndText',
              textPlace: 'right'
            },
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'birthday',
            title: 'Дата рождения',
            width: 100,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            sortable: true
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
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        columns: [
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'name',
            title: 'Имя',
            width: 100,
            dataIndex: 'name',
            key: 'name',
            className: 'name-cell',
            sortable: true,
            fixed: 'left'
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'IconCell',
              id: 'surname',
              icon: 'fa fa-plus',
              type: 'iconAndText',
              textPlace: 'right'
            },
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'birthday',
            title: 'Дата рождения',
            width: 200,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            sortable: true,
            fixed: 'right'
          }
        ],
        scroll: {
          x: '130%'
        }
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('Multi level header', () => {
    fetchMock.restore().get(urlPattern, url => getStubData(url));
    const props = {
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        multiHeader: true,
        columns: [
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'name',
            title: 'Имя',
            width: 100,
            dataIndex: 'name',
            key: 'name',
            className: 'name-cell',
            children: [
              {
                id: 'name2',
                title: 'Имя1',
                width: 100,
                dataIndex: 'name1',
                key: 'name1',
                className: 'name-cell'
              },
              {
                id: 'name2',
                title: 'Имя2',
                width: 100,
                dataIndex: 'name2',
                key: 'name2',
                className: 'name-cell'
              }
            ]
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'IconCell',
              id: 'surname',
              icon: 'fa fa-plus',
              type: 'iconAndText',
              textPlace: 'right'
            },
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            sortable: true
          },
          {
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            id: 'birthday',
            title: 'Дата рождения',
            width: 200,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            sortable: true
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
      ...metadata['Page_Table'],
      table: {
        ...metadata['Page_Table'].table,
        columns: [
          {
            id: 'name',
            title: 'Имя',
            width: 100,
            className: 'name-cell',
            dataIndex: 'name',
            key: 'name',
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            }
          },
          {
            id: 'surname',
            title: 'Фамилия',
            width: 100,
            dataIndex: 'surname',
            key: 'surname',
            className: 'surname-cell',
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'IconCell',
              id: 'surname',
              icon: 'fa fa-plus',
              type: 'iconAndText',
              textPlace: 'right'
            },
            editable: true,
            edit: {
              src: 'InputText'
            }
          },
          {
            id: 'birthday',
            title: 'Дата рождения',
            width: 100,
            dataIndex: 'birthday',
            key: 'birthday',
            className: 'birthday-cell',
            header: {
              src: 'TextTableHeader'
            },
            cell: {
              src: 'TextCell'
            },
            editable: true,
            edit: {
              src: 'InputNumber'
            }
          }
        ]
      }
    };
    return <Factory level={WIDGETS} {...props} id="Page_Table" />;
  })
  .add('nested', () => {
    return <div>nested</div>;
  });
