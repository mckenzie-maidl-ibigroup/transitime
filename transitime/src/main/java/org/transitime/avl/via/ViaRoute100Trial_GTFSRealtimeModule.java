package org.transitime.avl.via;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.transitime.avl.GtfsRealtimeModule;
import org.transitime.core.VehicleState;
import org.transitime.core.dataCache.ArrivalDeparturesToProcessHoldingTimesFor;
import org.transitime.core.dataCache.HoldingTimeCache;
import org.transitime.core.dataCache.VehicleStateManager;
import org.transitime.core.holdingmethod.HoldingTimeGeneratorFactory;
import org.transitime.db.structs.ArrivalDeparture;
import org.transitime.db.structs.AvlReport;
import org.transitime.db.structs.HoldingTime;
import org.transitime.db.structs.AvlReport.AssignmentType;
import org.transitime.feed.gtfsRt.GtfsRtVehiclePositionsReader;

public class ViaRoute100Trial_GTFSRealtimeModule extends GtfsRealtimeModule {

	public ViaRoute100Trial_GTFSRealtimeModule(String projectId) {
		super(projectId);		
	}

	@Override
	protected void getAndProcessData() {
		List<AvlReport> avlReports = GtfsRtVehiclePositionsReader
				.getAvlReports(getGtfsRealtimeURI());
		for (AvlReport avlReport : avlReports) {
			if (shouldProcessAvl)
			{			
				if(route100Trip(avlReport.getAssignmentId()))
				{
					// set to route as we are going to set this route up and frequency based with a single trip id.
					avlReport.setAssignment("100", AssignmentType.ROUTE_ID);
					processAvlReport(avlReport);
				}								
			}
			else
			{
				System.out.println(avlReport);
			}
		}
		
	}

	private static final String tripIds[]= {
			"3231408",
			"3231409",
			"3231406",
			"3231407",
			"3231404",
			"3231405",
			"3231877",
			"3231875",
			"3231874",
			"3231873",
			"3231871",
			"3231879",
			"3231878",
			"3231524",
			"3231856",
			"3231724",
			"3163978",
			"3163979",
			"3232159",
			"3162009",
			"3162008",
			"3162005",
			"3162004",
			"3162003",
			"3162002",
			"3162001",
			"3162000",
			"3232158",
			"3231653",
			"3231876",
			"3231651",
			"3231650",
			"3231657",
			"3231872",
			"3231870",
			"3231659",
			"3231658",
			"3161943",
			"3161942",
			"3161941",
			"3161940",
			"3161947",
			"3161946",
			"3161945",
			"3161944",
			"3161949",
			"3161948",
			"3232139",
			"3232138",
			"3232133",
			"3232132",
			"3232131",
			"3232130",
			"3232137",
			"3232136",
			"3232135",
			"3232134",
			"3231888",
			"3231578",
			"3231486",
			"3231487",
			"3231484",
			"3231485",
			"3231482",
			"3231483",
			"3231480",
			"3231481",
			"3231488",
			"3231489",
			"3231810",
			"3231675",
			"3231729",
			"3231728",
			"3231723",
			"3231722",
			"3231721",
			"3231720",
			"3231727",
			"3231726",
			"3164030",
			"3164031",
			"3164032",
			"3164033",
			"3164034",
			"3164035",
			"3164036",
			"3164037",
			"3164038",
			"3164039",
			"3231677",
			"3231671",
			"3231673",
			"3163819",
			"3163818",
			"3163817",
			"3163816",
			"3231784",
			"3231963",
			"3231593",
			"3231782",
			"3162124",
			"3162125",
			"3162126",
			"3162120",
			"3162121",
			"3162122",
			"3162123",
			"3163756",
			"3163757",
			"3163750",
			"3163751",
			"3163752",
			"3163753",
			"3163758",
			"3163759",
			"3161860",
			"3161861",
			"3161862",
			"3161863",
			"3161864",
			"3161865",
			"3161866",
			"3161867",
			"3161868",
			"3161869",
			"3231674",
			"3231676",
			"3231672",
			"3232047",
			"3232046",
			"3232045",
			"3232044",
			"3232043",
			"3232042",
			"3232041",
			"3232040",
			"3232049",
			"3232048",
			"3231572",
			"3231573",
			"3231570",
			"3231571",
			"3231574",
			"3231575",
			"3163838",
			"3163831",
			"3163830",
			"3163833",
			"3163832",
			"3163835",
			"3163834",
			"3163837",
			"3231909",
			"3231908",
			"3231903",
			"3231902",
			"3231901",
			"3231900",
			"3231907",
			"3231906",
			"3231905",
			"3231904",
			"3161994",
			"3161995",
			"3161996",
			"3161997",
			"3161990",
			"3161991",
			"3161992",
			"3161993",
			"3161998",
			"3161999",
			"3163974",
			"3163975",
			"3163976",
			"3163977",
			"3163970",
			"3163971",
			"3163972",
			"3162007",
			"3162006",
			"3231439",
			"3231438",
			"3232062",
			"3163656",
			"3163659",
			"3163658",
			"3231739",
			"3231732",
			"3231733",
			"3231735",
			"3161818",
			"3161812",
			"3231530",
			"3231835",
			"3231834",
			"3231640",
			"3231641",
			"3231644",
			"3231645",
			"3231646",
			"3231647",
			"3231983",
			"3231982",
			"3231981",
			"3231980",
			"3231987",
			"3231986",
			"3231985",
			"3231984",
			"3231989",
			"3231988",
			"3161918",
			"3161919",
			"3161914",
			"3161915",
			"3161916",
			"3161917",
			"3161910",
			"3161911",
			"3232068",
			"3231595",
			"3231820",
			"3231821",
			"3231822",
			"3231823",
			"3161891",
			"3161890",
			"3161893",
			"3161892",
			"3161895",
			"3161894",
			"3161897",
			"3161896",
			"3161899",
			"3161898",
			"3231504",
			"3231503",
			"3231502",
			"3231774",
			"3231776",
			"3231777",
			"3231770",
			"3231771",
			"3231772",
			"3231500",
			"3231778",
			"3164049",
			"3164048",
			"3164041",
			"3164040",
			"3164043",
			"3164042",
			"3164045",
			"3164044",
			"3164047",
			"3164046",
			"3163905",
			"3163904",
			"3163907",
			"3163906",
			"3163901",
			"3163900",
			"3163903",
			"3163902",
			"3163909",
			"3163908",
			"3162058",
			"3162059",
			"3163853",
			"3163852",
			"3163851",
			"3163855",
			"3162096",
			"3231424",
			"3231425",
			"3161819",
			"3161813",
			"3161815",
			"3161814",
			"3161817",
			"3161816",
			"3231766",
			"3231764",
			"3231761",
			"3231661",
			"3163989",
			"3163988",
			"3163985",
			"3163984",
			"3163987",
			"3163986",
			"3163981",
			"3163980",
			"3163983",
			"3163982",
			"3231512",
			"3163761",
			"3163763",
			"3163762",
			"3163765",
			"3163764",
			"3163767",
			"3163766",
			"3231518",
			"3231549",
			"3231548",
			"3231543",
			"3231542",
			"3231541",
			"3231547",
			"3231546",
			"3231545",
			"3231544",
			"3162041",
			"3231954",
			"3231955",
			"3231956",
			"3231957",
			"3231950",
			"3231951",
			"3231952",
			"3231953",
			"3231958",
			"3231959",
			"3163725",
			"3163724",
			"3163727",
			"3163726",
			"3163721",
			"3163720",
			"3163723",
			"3163722",
			"3163729",
			"3163728",
			"3231623",
			"3231620",
			"3231621",
			"3231412",
			"3231418",
			"3231639",
			"3231638",
			"3231631",
			"3231630",
			"3231635",
			"3231634",
			"3231637",
			"3231636",
			"3163868",
			"3163869",
			"3163862",
			"3163863",
			"3163860",
			"3163861",
			"3163866",
			"3163867",
			"3163864",
			"3163865",
			"3231625",
			"3231628",
			"3231629",
			"3231513",
			"3231468",
			"3231469",
			"3231460",
			"3231461",
			"3231462",
			"3231463",
			"3231464",
			"3231465",
			"3231466",
			"3231467",
			"3232078",
			"3232079",
			"3232070",
			"3231851",
			"3231850",
			"3231853",
			"3231852",
			"3231855",
			"3231854",
			"3231540",
			"3166007",
			"3166006",
			"3164092",
			"3164093",
			"3164090",
			"3164091",
			"3164096",
			"3164097",
			"3164094",
			"3164095",
			"3164098",
			"3164099",
			"3232090",
			"3232091",
			"3232092",
			"3232093",
			"3232094",
			"3232095",
			"3232096",
			"3232097",
			"3232098",
			"3232099",
			"3163958",
			"3163959",
			"3163956",
			"3163957",
			"3163954",
			"3163955",
			"3163952",
			"3163953",
			"3163950",
			"3163951",
			"3162029",
			"3162028",
			"3231936",
			"3161969",
			"3161968",
			"3161965",
			"3161964",
			"3161967",
			"3161966",
			"3161961",
			"3161960",
			"3161963",
			"3161962",
			"3232115",
			"3232114",
			"3232117",
			"3232116",
			"3232111",
			"3232110",
			"3232113",
			"3232112",
			"3232119",
			"3232118",
			"3231725",
			"3232109",
			"3163688",
			"3163689",
			"3163682",
			"3163683",
			"3163680",
			"3163681",
			"3163686",
			"3163687",
			"3163684",
			"3163685",
			"3232035",
			"3231705",
			"3231707",
			"3231706",
			"3231701",
			"3231700",
			"3231703",
			"3231709",
			"3231708",
			"3164018",
			"3164019",
			"3164012",
			"3164013",
			"3164010",
			"3164011",
			"3164016",
			"3164017",
			"3164014",
			"3164015",
			"3232038",
			"3231798",
			"3231516",
			"3162106",
			"3162107",
			"3162104",
			"3162105",
			"3162102",
			"3162103",
			"3162100",
			"3162101",
			"3162108",
			"3162109",
			"3163778",
			"3163779",
			"3163774",
			"3163775",
			"3163772",
			"3163773",
			"3163770",
			"3163771",
			"3161848",
			"3161849",
			"3161842",
			"3161843",
			"3161840",
			"3161841",
			"3161846",
			"3161847",
			"3161844",
			"3161845",
			"3231450",
			"3231789",
			"3231788",
			"3231785",
			"3231787",
			"3231786",
			"3231781",
			"3231780",
			"3231783",
			"3161828",
			"3232061",
			"3232060",
			"3232063",
			"3232065",
			"3232064",
			"3232067",
			"3232066",
			"3232069",
			"3231849",
			"3231598",
			"3231599",
			"3231594",
			"3231596",
			"3231597",
			"3231590",
			"3231591",
			"3231592",
			"3231711",
			"3163857",
			"3163859",
			"3163858",
			"3231717",
			"3231925",
			"3231924",
			"3231927",
			"3231926",
			"3231921",
			"3231920",
			"3231923",
			"3231922",
			"3231929",
			"3231928",
			"3231411",
			"3231410",
			"3231415",
			"3231414",
			"3231417",
			"3231416",
			"3232003",
			"3232002",
			"3232001",
			"3163967",
			"3163966",
			"3163965",
			"3163964",
			"3163963",
			"3163962",
			"3163961",
			"3163960",
			"3163969",
			"3163968",
			"3231918",
			"3231919",
			"3231913",
			"3231916",
			"3231917",
			"3231668",
			"3231669",
			"3231663",
			"3231660",
			"3231666",
			"3231667",
			"3231664",
			"3231665",
			"3161936",
			"3161937",
			"3161934",
			"3161935",
			"3161932",
			"3161933",
			"3161930",
			"3161931",
			"3161938",
			"3161939",
			"3232148",
			"3232149",
			"3232146",
			"3232147",
			"3232144",
			"3232145",
			"3232142",
			"3232143",
			"3232140",
			"3232141",
			"3231867",
			"3231769",
			"3231499",
			"3231498",
			"3231491",
			"3231490",
			"3231493",
			"3231492",
			"3231495",
			"3231494",
			"3231497",
			"3231496",
			"3231802",
			"3231803",
			"3231800",
			"3231801",
			"3231807",
			"3231805",
			"3231809",
			"3231758",
			"3231759",
			"3231756",
			"3231757",
			"3231754",
			"3231755",
			"3231752",
			"3231753",
			"3231750",
			"3231751",
			"3164023",
			"3164022",
			"3164021",
			"3164020",
			"3164027",
			"3164026",
			"3164025",
			"3164024",
			"3164029",
			"3164028",
			"3162078",
			"3162079",
			"3162072",
			"3162073",
			"3162070",
			"3162071",
			"3162076",
			"3162077",
			"3162074",
			"3162075",
			"3163754",
			"3163755",
			"3231531",
			"3163803",
			"3163884",
			"3163885",
			"3163886",
			"3163887",
			"3163880",
			"3163881",
			"3163882",
			"3163883",
			"3163888",
			"3163889",
			"3231893",
			"3231649",
			"3163747",
			"3163746",
			"3163745",
			"3163744",
			"3163743",
			"3163742",
			"3163741",
			"3163740",
			"3163749",
			"3163748",
			"3232018",
			"3232019",
			"3232010",
			"3232011",
			"3232012",
			"3232013",
			"3232014",
			"3232015",
			"3232016",
			"3232017",
			"3161873",
			"3161872",
			"3161871",
			"3161870",
			"3161877",
			"3161876",
			"3161874",
			"3161879",
			"3161878",
			"3231420",
			"3231421",
			"3231428",
			"3231429",
			"3231806",
			"3231804",
			"3231808",
			"3231565",
			"3231564",
			"3231567",
			"3231566",
			"3231561",
			"3231563",
			"3231562",
			"3231569",
			"3231568",
			"3163808",
			"3163809",
			"3163804",
			"3163806",
			"3163807",
			"3163800",
			"3163801",
			"3163802",
			"3231978",
			"3231979",
			"3231976",
			"3231977",
			"3231974",
			"3231975",
			"3231972",
			"3231973",
			"3231970",
			"3231971",
			"3232081",
			"3161987",
			"3161986",
			"3161985",
			"3161984",
			"3161983",
			"3161982",
			"3161981",
			"3161980",
			"3161989",
			"3161988",
			"3231633",
			"3231632",
			"3231603",
			"3163668",
			"3163669",
			"3231534",
			"3231533",
			"3231814",
			"3161909",
			"3161908",
			"3161907",
			"3161906",
			"3161905",
			"3161904",
			"3161903",
			"3161902",
			"3161901",
			"3161900",
			"3232101",
			"3232107",
			"3231442",
			"3231443",
			"3231440",
			"3231441",
			"3231446",
			"3231447",
			"3231444",
			"3231445",
			"3231448",
			"3231449",
			"3231839",
			"3231838",
			"3231833",
			"3231832",
			"3231831",
			"3231830",
			"3231837",
			"3231836",
			"3231858",
			"3231767",
			"3231763",
			"3231762",
			"3231760",
			"3163716",
			"3163717",
			"3164078",
			"3164079",
			"3164074",
			"3164075",
			"3164076",
			"3164077",
			"3164070",
			"3164071",
			"3164072",
			"3164073",
			"3163938",
			"3163939",
			"3163930",
			"3163931",
			"3163932",
			"3163933",
			"3163934",
			"3163935",
			"3163936",
			"3163937",
			"3162043",
			"3162040",
			"3162047",
			"3162046",
			"3162045",
			"3162044",
			"3162049",
			"3162048",
			"3231738",
			"3163843",
			"3161829",
			"3161824",
			"3161825",
			"3161826",
			"3161827",
			"3161820",
			"3161821",
			"3161822",
			"3161823",
			"3231432",
			"3231817",
			"3231816",
			"3231678",
			"3231857",
			"3231684",
			"3231685",
			"3231686",
			"3231687",
			"3231680",
			"3231681",
			"3231682",
			"3231683",
			"3231688",
			"3231689",
			"3231538",
			"3231539",
			"3231536",
			"3231537",
			"3231535",
			"3231532",
			"3231431",
			"3231768",
			"3231947",
			"3231946",
			"3231945",
			"3231944",
			"3231943",
			"3231942",
			"3231941",
			"3231940",
			"3231949",
			"3231948",
			"3231506",
			"3231505",
			"3163718",
			"3163719",
			"3163710",
			"3163711",
			"3163712",
			"3231501",
			"3163973",
			"3231577",
			"3231997",
			"3231579",
			"3231670",
			"3163839",
			"3163836",
			"3232009",
			"3232008",
			"3162094",
			"3162095",
			"3162097",
			"3162098",
			"3162099",
			"3232108",
			"3231608",
			"3231604",
			"3231600",
			"3231601",
			"3231602",
			"3232026",
			"3232021",
			"3232020",
			"3232022",
			"3232029",
			"3232028",
			"3163879",
			"3163878",
			"3163875",
			"3163874",
			"3163877",
			"3163876",
			"3163871",
			"3163870",
			"3163873",
			"3163872",
			"3163790",
			"3163791",
			"3163792",
			"3163793",
			"3163794",
			"3163795",
			"3163796",
			"3163797",
			"3163798",
			"3163799",
			"3231713",
			"3231719",
			"3232151",
			"3232150",
			"3232153",
			"3232152",
			"3232155",
			"3232154",
			"3232157",
			"3232156",
			"3231479",
			"3231478",
			"3231473",
			"3231472",
			"3231471",
			"3231470",
			"3231477",
			"3231476",
			"3231475",
			"3231474",
			"3231840",
			"3231864",
			"3231865",
			"3231866",
			"3231860",
			"3231862",
			"3231863",
			"3231868",
			"3231869",
			"3162056",
			"3166074",
			"3166075",
			"3231437",
			"3231435",
			"3164085",
			"3164084",
			"3164087",
			"3164086",
			"3164081",
			"3164080",
			"3164083",
			"3164082",
			"3164089",
			"3164088",
			"3232083",
			"3232082",
			"3232089",
			"3232088",
			"3163941",
			"3163940",
			"3163943",
			"3163945",
			"3163944",
			"3163947",
			"3163946",
			"3163949",
			"3163948",
			"3162018",
			"3162019",
			"3162014",
			"3162016",
			"3162017",
			"3162010",
			"3162011",
			"3162012",
			"3162013",
			"3231847",
			"3231844",
			"3231843",
			"3231841",
			"3161950",
			"3161951",
			"3161952",
			"3161953",
			"3161954",
			"3161955",
			"3161956",
			"3161957",
			"3161958",
			"3161959",
			"3231662",
			"3231861",
			"3232128",
			"3232129",
			"3232120",
			"3232121",
			"3232122",
			"3232123",
			"3232124",
			"3232125",
			"3232126",
			"3232127",
			"3232025",
			"3162021",
			"3162020",
			"3162023",
			"3162022",
			"3162025",
			"3162024",
			"3162027",
			"3162026",
			"3232024",
			"3232000",
			"3232007",
			"3232006",
			"3232005",
			"3232004",
			"3162090",
			"3162091",
			"3162092",
			"3162093",
			"3163699",
			"3163698",
			"3163695",
			"3163694",
			"3163697",
			"3163696",
			"3163691",
			"3163690",
			"3163693",
			"3163692",
			"3231889",
			"3231882",
			"3231883",
			"3231880",
			"3231881",
			"3231886",
			"3231887",
			"3231884",
			"3231885",
			"3231436",
			"3231434",
			"3231433",
			"3231430",
			"3231730",
			"3231731",
			"3231734",
			"3231736",
			"3231737",
			"3164009",
			"3164008",
			"3164005",
			"3164004",
			"3164007",
			"3164006",
			"3164001",
			"3164000",
			"3164003",
			"3164002",
			"3231765",
			"3231815",
			"3231811",
			"3231813",
			"3231812",
			"3231818",
			"3232027",
			"3231509",
			"3231508",
			"3162119",
			"3162118",
			"3162111",
			"3162110",
			"3162113",
			"3162112",
			"3162115",
			"3162114",
			"3162117",
			"3162116",
			"3232023",
			"3231609",
			"3231605",
			"3231606",
			"3231607",
			"3163760",
			"3163769",
			"3163768",
			"3161859",
			"3161858",
			"3161855",
			"3161854",
			"3161857",
			"3161856",
			"3161851",
			"3161850",
			"3161853",
			"3161852",
			"3163840",
			"3231523",
			"3231522",
			"3231529",
			"3231528",
			"3231627",
			"3231624",
			"3231622",
			"3231859",
			"3163657",
			"3232031",
			"3232054",
			"3232055",
			"3232056",
			"3232057",
			"3232050",
			"3232051",
			"3232052",
			"3232053",
			"3232058",
			"3232059",
			"3231589",
			"3231588",
			"3231587",
			"3231586",
			"3231585",
			"3231584",
			"3231583",
			"3231582",
			"3231581",
			"3231580",
			"3163826",
			"3163827",
			"3163824",
			"3163825",
			"3163822",
			"3163823",
			"3163820",
			"3163821",
			"3163828",
			"3163829",
			"3231910",
			"3231911",
			"3231912",
			"3231914",
			"3231915",
			"3232032",
			"3232033",
			"3232036",
			"3232037",
			"3232034",
			"3231426",
			"3231427",
			"3231422",
			"3231423",
			"3231744",
			"3163702",
			"3163701",
			"3163704",
			"3231679",
			"3231507",
			"3231990",
			"3231991",
			"3231992",
			"3231993",
			"3231994",
			"3231995",
			"3231996",
			"3231998",
			"3231999",
			"3161929",
			"3161928",
			"3161921",
			"3161920",
			"3161922",
			"3161925",
			"3161924",
			"3161927",
			"3161926",
			"3231819",
			"3161888",
			"3161889",
			"3161886",
			"3161887",
			"3161884",
			"3161885",
			"3161882",
			"3161883",
			"3161880",
			"3161881",
			"3231842",
			"3231741",
			"3231740",
			"3231743",
			"3231742",
			"3231745",
			"3231747",
			"3231746",
			"3231749",
			"3231748",
			"3164056",
			"3164057",
			"3164054",
			"3164055",
			"3164052",
			"3164053",
			"3164050",
			"3164051",
			"3164058",
			"3164059",
			"3163912",
			"3163913",
			"3163910",
			"3163911",
			"3163916",
			"3163917",
			"3163914",
			"3163915",
			"3163918",
			"3163919",
			"3162069",
			"3162068",
			"3162065",
			"3162064",
			"3162067",
			"3162066",
			"3162061",
			"3162060",
			"3162063",
			"3162062",
			"3161923",
			"3231413",
			"3231642",
			"3231643",
			"3231626",
			"3163897",
			"3163896",
			"3163895",
			"3163894",
			"3163893",
			"3163892",
			"3163891",
			"3163890",
			"3163899",
			"3163898",
			"3231899",
			"3163850",
			"3163856",
			"3163854",
			"3231560",
			"3231652",
			"3163998",
			"3163999",
			"3163992",
			"3163993",
			"3163990",
			"3163991",
			"3163996",
			"3163997",
			"3163994",
			"3163995",
			"3231576",
			"3163805",
			"3161912",
			"3161913",
			"3231656",
			"3231655",
			"3231654",
			"3231799",
			"3231791",
			"3231558",
			"3231559",
			"3231550",
			"3231551",
			"3231552",
			"3231553",
			"3231554",
			"3231555",
			"3231556",
			"3231557",
			"3163815",
			"3163814",
			"3163813",
			"3163812",
			"3163811",
			"3163810",
			"3231961",
			"3231960",
			"3231962",
			"3231965",
			"3231964",
			"3231967",
			"3231966",
			"3231969",
			"3231968",
			"3163732",
			"3163733",
			"3163730",
			"3163731",
			"3163736",
			"3163737",
			"3163734",
			"3163735",
			"3163738",
			"3163739",
			"3231825",
			"3231704",
			"3231702",
			"3232164",
			"3232160",
			"3232161",
			"3232162",
			"3232163",
			"3163677",
			"3163676",
			"3163675",
			"3163674",
			"3163673",
			"3163672",
			"3163671",
			"3163670",
			"3163679",
			"3163678",
			"3163707",
			"3232080",
			"3232087",
			"3232086",
			"3232085",
			"3232084",
			"3163942",
			"3162015",
			"3231419",
			"3231510",
			"3231511",
			"3231455",
			"3231454",
			"3231457",
			"3231456",
			"3231451",
			"3231453",
			"3231452",
			"3231459",
			"3231458",
			"3231828",
			"3231829",
			"3232076",
			"3232077",
			"3232074",
			"3232075",
			"3232072",
			"3232073",
			"3232071",
			"3231519",
			"3231824",
			"3231848",
			"3231846",
			"3231845",
			"3231895",
			"3231894",
			"3231897",
			"3231891",
			"3231890",
			"3164069",
			"3164068",
			"3164067",
			"3164066",
			"3164065",
			"3164064",
			"3164063",
			"3164062",
			"3164061",
			"3164060",
			"3163929",
			"3163928",
			"3163923",
			"3163922",
			"3163921",
			"3163920",
			"3163927",
			"3163926",
			"3163925",
			"3163924",
			"3162036",
			"3162037",
			"3162034",
			"3162032",
			"3162033",
			"3162030",
			"3162031",
			"3162038",
			"3162039",
			"3231826",
			"3231827",
			"3162042",
			"3161978",
			"3161979",
			"3161972",
			"3161973",
			"3161970",
			"3161971",
			"3161976",
			"3161977",
			"3161974",
			"3161975",
			"3232102",
			"3232103",
			"3232100",
			"3232106",
			"3232104",
			"3232105",
			"3161839",
			"3161838",
			"3161837",
			"3161836",
			"3161835",
			"3161834",
			"3161833",
			"3161832",
			"3161831",
			"3161830",
			"3163841",
			"3163842",
			"3163844",
			"3163845",
			"3163846",
			"3231514",
			"3231515",
			"3231712",
			"3163847",
			"3231710",
			"3231517",
			"3231716",
			"3231714",
			"3231715",
			"3231718",
			"3163664",
			"3163665",
			"3163666",
			"3163667",
			"3163660",
			"3163661",
			"3163662",
			"3163663",
			"3231932",
			"3231931",
			"3231697",
			"3231696",
			"3231695",
			"3231694",
			"3231693",
			"3231692",
			"3231691",
			"3231690",
			"3231699",
			"3231698",
			"3231521",
			"3231520",
			"3231525",
			"3231527",
			"3231526",
			"3231648",
			"3163709",
			"3163708",
			"3163703",
			"3163700",
			"3163706",
			"3163705",
			"3163783",
			"3163786",
			"3163788",
			"3231775",
			"3231773",
			"3231779",
			"3231792",
			"3231793",
			"3231790",
			"3231796",
			"3231797",
			"3231794",
			"3231795",
			"3163776",
			"3163777",
			"3162087",
			"3162086",
			"3162085",
			"3162084",
			"3162083",
			"3162082",
			"3162081",
			"3162080",
			"3162089",
			"3162088",
			"3231619",
			"3231618",
			"3231617",
			"3231616",
			"3231615",
			"3231614",
			"3231613",
			"3231612",
			"3231611",
			"3231610",
			"3162050",
			"3162051",
			"3162052",
			"3162053",
			"3162054",
			"3162055",
			"3162057",
			"3163848",
			"3163849",
			"3231933",
			"3231930",
			"3231937",
			"3231934",
			"3231935",
			"3231938",
			"3231939",
			"3232030",
			"3163782",
			"3163781",
			"3163780",
			"3163787",
			"3163785",
			"3163784",
			"3163789",
			"3232039",
			"3231898",
			"3163713",
			"3163714",
			"3163715",
			"3231896",
			"3231892"
	};
	private static final ArrayList<String> list=new ArrayList<String>(Arrays.asList(tripIds));
	private boolean route100Trip(String assignmentId) {		
		if(list.contains(assignmentId))
		{
			return true;
		}else
		{
			return false;
		}		
	}

}