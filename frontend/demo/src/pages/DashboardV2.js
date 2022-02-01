import React, { Component } from 'react';
import ReactDOM from 'react-dom'
import { Container, Row, Col } from 'reactstrap';
import { AreaChart, Area, XAxis, YAxis, ResponsiveContainer, BarChart, Bar, LineChart, Line } from 'recharts';
import Trend from 'react-trend';
import ReactMapGL from 'react-map-gl';
import Moment from 'moment';
import momentLocalizer from 'react-widgets-moment';
import Calendar from 'react-widgets/lib/Calendar';

import Panel from 'n2o-framework/lib//components/snippets/Panel/PanelShortHand';
import Template from '../components/core/Template';

import LastTask from '../components/dashboard/LastTask';

import 'mapbox-gl/dist/mapbox-gl.css';
import 'react-widgets/dist/css/react-widgets.css';

const data = [{"name":"00:00","Google":594,"Yandex":495,"Bing":390,"Yahoo":209,"Ask":132,"Baidu":53},{"name":"01:00","Google":508,"Yandex":470,"Bing":372,"Yahoo":231,"Ask":155,"Baidu":67},{"name":"02:00","Google":586,"Yandex":436,"Bing":390,"Yahoo":253,"Ask":194,"Baidu":81},{"name":"03:00","Google":538,"Yandex":428,"Bing":327,"Yahoo":251,"Ask":161,"Baidu":91},{"name":"04:00","Google":500,"Yandex":471,"Bing":306,"Yahoo":257,"Ask":191,"Baidu":88},{"name":"05:00","Google":508,"Yandex":442,"Bing":313,"Yahoo":231,"Ask":196,"Baidu":51},{"name":"06:00","Google":591,"Yandex":434,"Bing":314,"Yahoo":238,"Ask":185,"Baidu":85},{"name":"07:00","Google":510,"Yandex":453,"Bing":348,"Yahoo":261,"Ask":175,"Baidu":66},{"name":"08:00","Google":501,"Yandex":415,"Bing":301,"Yahoo":242,"Ask":161,"Baidu":71},{"name":"09:00","Google":537,"Yandex":439,"Bing":317,"Yahoo":297,"Ask":164,"Baidu":53},{"name":"10:00","Google":508,"Yandex":422,"Bing":331,"Yahoo":231,"Ask":111,"Baidu":66},{"name":"11:00","Google":528,"Yandex":496,"Bing":363,"Yahoo":222,"Ask":191,"Baidu":57},{"name":"12:00","Google":520,"Yandex":498,"Bing":320,"Yahoo":247,"Ask":132,"Baidu":70},{"name":"13:00","Google":508,"Yandex":415,"Bing":397,"Yahoo":278,"Ask":158,"Baidu":64},{"name":"14:00","Google":513,"Yandex":410,"Bing":301,"Yahoo":252,"Ask":151,"Baidu":78},{"name":"15:00","Google":507,"Yandex":441,"Bing":339,"Yahoo":208,"Ask":176,"Baidu":74},{"name":"16:00","Google":598,"Yandex":496,"Bing":397,"Yahoo":242,"Ask":192,"Baidu":52},{"name":"17:00","Google":532,"Yandex":471,"Bing":319,"Yahoo":295,"Ask":106,"Baidu":59},{"name":"18:00","Google":600,"Yandex":427,"Bing":361,"Yahoo":213,"Ask":174,"Baidu":65},{"name":"19:00","Google":577,"Yandex":461,"Bing":300,"Yahoo":289,"Ask":124,"Baidu":54},{"name":"20:00","Google":548,"Yandex":484,"Bing":392,"Yahoo":289,"Ask":161,"Baidu":90},{"name":"21:00","Google":535,"Yandex":425,"Bing":308,"Yahoo":275,"Ask":127,"Baidu":77},{"name":"22:00","Google":503,"Yandex":452,"Bing":327,"Yahoo":202,"Ask":188,"Baidu":99},{"name":"23:00","Google":560,"Yandex":407,"Bing":303,"Yahoo":277,"Ask":180,"Baidu":58},{"name":"00:00","Google":553,"Yandex":477,"Bing":376,"Yahoo":278,"Ask":160,"Baidu":54}];
const MAPBOX_TOKEN = 'pk.eyJ1IjoiZW1hbW9zaGluIiwiYSI6ImNqZXM5b3BiOTBqYTUyd3FrY2c4Y3dqdXYifQ.m69lzEf29wCEvQApujgYew';
const dataUsers = [
  { name: '1', uv: 4400, pv: 2013, amt: 4500, time: 1, uvError: [100, 50], pvError: [110, 20] },
  { name: '2', uv: 3300, pv: 2000, amt: 6500, time: 2, uvError: 120, pvError: 50 },
  { name: '3', uv: 3200, pv: 1398, amt: 5000, time: 3, uvError: [120, 80], pvError: [200, 100] },
  { name: '4', uv: 5000, pv: 2800, amt: 4000, time: 4, uvError: 100, pvError: 30 },
  { name: '5', uv: 6000, pv: 2800, amt: 4000, time: 4, uvError: 100, pvError: 30 },
  { name: '6', uv: 3400, pv: 2800, amt: 4000, time: 4, uvError: 100, pvError: 30 },
  { name: '7', uv: 4500, pv: 2800, amt: 4000, time: 4, uvError: 100, pvError: 30 },
  { name: '8', uv: 2000, pv: 2800, amt: 4000, time: 4, uvError: 100, pvError: 30 },
  { name: '9', uv: 2300, pv: 2800, amt: 4000, time: 4, uvError: 100, pvError: 30 },
  { name: '10', uv: 3400, pv: 2800, amt: 4000, time: 4, uvError: 100, pvError: 30 },
];

const tabs = [
  {
    id: '1',
    header: 'Трекер задач',
    content: (<LastTask />)
  },
  {
    id: '2',
    header: 'Запросы',
    content: (<LastTask />)
  },
];

Moment.locale('ru');
momentLocalizer();

class DashboardV2 extends Component {
  constructor(props){
    super(props);

    this.state = {
      viewport: {
        latitude: 30.47201281323611,
        longitude: 10.626289673184305,
        zoom: 0.6,
        bearing: 0,
        pitch: 0,
        width: 500,
        height: 500
      }
    };

    this._resize = this._resize.bind(this);
    this._onViewportChange = this._onViewportChange.bind(this);
  }

  componentDidMount() {
    window.addEventListener('resize', this._resize);
    this._resize();
  }

  componentWillUnmount() {
    window.removeEventListener('resize', this._resize);
  }

  _resize() {
    const mapDom = ReactDOM.findDOMNode(this._mapContainer);
    this.setState({
      viewport: {
        ...this.state.viewport,
        width: mapDom.offsetWidth - 30,
      }
    });
  };

  _onViewportChange(viewport) {
    this.setState({viewport});
  }

  render() {
    return (
      <Template>
        <Container fluid>
          <Row className='mb-3'>
            <Col md={3}>
              <div className="card" style={{backgroundColor: '#26a69a', borderColor: '#26a69a', color: '#fff'}}>
                <div className="card-body">
                  <h3 style={{margin: 0}}>3,450 <span className="badge" style={{backgroundColor: '#00695c', borderColor: '#00695c', color: '#fff'}}>+53,6%</span></h3>
                  Пользователей
                </div>
                <div className="container-fluid" style={{height: 40}}>
                  <ResponsiveContainer>
                    <BarChart width={150} height={40} data={dataUsers}>
                      <Bar dataKey="uv" fill="#93d3cd" />
                    </BarChart>
                  </ResponsiveContainer>
                </div>
              </div>
            </Col>
            <Col md={3}>
              <div className="card" style={{backgroundColor: '#E91E63', borderColor: '#E91E63', color: '#fff'}}>
                <div className="card-body">
                  <h3 style={{margin: 0}}>3,450 <span className="badge" style={{backgroundColor: '#ad1457', borderColor: '#ad1457', color: '#fff'}}>+53,6%</span></h3>
                  Пользователей
                </div>
                <div className="container-fluid" style={{height: 40}}>
                  <ResponsiveContainer>
                    <AreaChart data={dataUsers}
                               margin={{top: 5, right: 0, left: 0, bottom: 5}}>
                      <Area type='monotone' dataKey='uv' stroke='rgba(255, 255, 255, 0.6)'
                            fill='rgba(255, 255, 255, 0.4)' />
                    </AreaChart>
                  </ResponsiveContainer>
                </div>
              </div>
            </Col>
            <Col md={3}>
              <div className="card" style={{backgroundColor: '#3F51B5', borderColor: '#3F51B5', color: '#fff'}}>
                <div className="card-body">
                  <h3 style={{margin: 0}}>3,450 <span className="badge" style={{backgroundColor: '#283593', borderColor: '#283593', color: '#fff'}}>+53,6%</span></h3>
                  Пользователей
                </div>
                <div className="container-fluid" style={{height: 40}}>
                  <ResponsiveContainer>
                    <LineChart data={dataUsers}>
                      <Line type='monotone' dataKey='uv' stroke='rgba(255, 255, 255, 0.4)' strokeWidth={2} />
                    </LineChart>
                  </ResponsiveContainer>
                </div>
              </div>
            </Col>
          </Row>
          <Row className='mb-3'>
            <Col md={6}>
              <Panel headerTitle="Карта" className='mb-3' collapsible fullScreen icon="fa fa-globe"
                     ref={(el) => this._mapContainer = el}>
                <div className="row">
                  <div className="col-md-4">
                    <Trend
                      smooth
                      autoDraw
                      autoDrawDuration={2000}
                      autoDrawEasing="ease-out"
                      data={[0,2,5,9,5,8,3,5,0,3,2,7,5,9,11]}
                      gradient={['#337ab7', '#47a8ff']}
                      radius={10}
                      strokeWidth={2}
                      strokeLinecap={'round'}
                    />
                    <h5 style={{textAlign: 'center'}}><strong>Россия <sup className="text-success">117%</sup></strong></h5>
                  </div>
                  <div className="col-md-4">
                    <Trend
                      smooth
                      autoDraw
                      autoDrawDuration={2000}
                      autoDrawEasing="ease-out"
                      data={[0,2,5,3,7,3,4,6,4,2,7,8,10,11,7]}
                      gradient={['#d43f3a', '#ff4c47']}
                      radius={10}
                      strokeWidth={2}
                      strokeLinecap={'round'}
                    />
                    <h4 style={{textAlign: 'center'}}>США <sup className="text-success">76%</sup></h4>
                  </div>
                  <div className="col-md-4">
                    <Trend
                      smooth
                      autoDraw
                      autoDrawDuration={2000}
                      autoDrawEasing="ease-out"
                      data={[0,2,5,9,5,6,3,10,2,3,10,8,7,8,-2]}
                      gradient={['#4cae4c', '#5ece5e']}
                      radius={10}
                      strokeWidth={2}
                      strokeLinecap={'round'}
                    />
                    <h4 style={{textAlign: 'center'}}>Китай <sup className="text-danger">-25%</sup></h4>
                  </div>
                </div>
                <ReactMapGL
                  {...this.state.viewport}
                  mapStyle="mapbox://styles/mapbox/light-v9"
                  mapboxApiAccessToken={MAPBOX_TOKEN}
                  onViewportChange={this._onViewportChange} />
              </Panel>
              <Panel color='primary' headerTitle="Календарь" collapsible icon="fa fa-calendar"
                     className="panel-demo-flat-slate mb-3">
                <Calendar className="demo-calendar" />
              </Panel>
            </Col>
            <Col md={6}>
              <Panel color='info' headerTitle="Статистика переходов" collapsible icon="fa fa-area-chart"
                     className="panel-demo-flat-blue mb-3">
                <AreaChart width={this.state.viewport.width} height={400} data={data} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
                  <XAxis dataKey="name" tick={{ fill: '#ffffff' }} axisLine={false} tickLine={false} />
                  <YAxis tick={{ fill: '#ffffff' }} axisLine={false} tickLine={false} />
                  <Area type='monotone' dataKey='Google' stackId="1" stroke='#ffffff' fill='#ffffff' fillOpacity={0.8} />
                  <Area type='monotone' dataKey='Yandex' stackId="1" stroke='#ffffff' fill='#ffffff' fillOpacity={0.6} />
                  <Area type='monotone' dataKey='Bing' stackId="1" stroke='#ffffff' fill='#ffffff' fillOpacity={0.4} />
                  <Area type='monotone' dataKey='Yahoo' stackId="1" stroke='#ffffff' fill='#ffffff' fillOpacity={0.2} />
                  <Area type='monotone' dataKey='Ask' stackId="1" stroke='#ffffff' fill='#ffffff' fillOpacity={0.1} />
                </AreaChart>
              </Panel>
              <Panel className='mb-3' headerTitle="Активность" collapsible fullScreen tabs={tabs} hasTabs/>
            </Col>
          </Row>
        </Container>
      </Template>
    );
  }
}

export default DashboardV2;
