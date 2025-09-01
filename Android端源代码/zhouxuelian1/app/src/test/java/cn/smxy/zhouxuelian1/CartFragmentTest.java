//package cn.smxy.zhouxuelian1;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.mockito.Mockito.*;
//import static org.junit.Assert.*;
//
//import cn.smxy.zhouxuelian1.dao.CakeCartDao;
//import cn.smxy.zhouxuelian1.entity.Cake;
//import cn.smxy.zhouxuelian1.utils.MyDBHelper;
//
//@RunWith(AndroidJUnit4.class)
//public class CartFragmentTest {
//
//    @Mock
//    private SQLiteDatabase db;
//    @Mock
//    private MyDBHelper dbHelper;
//
//    private CakeCartDao cakeCartDao;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        cakeCartDao = new CakeCartDao(null); // Context can be null in this case
//        cakeCartDao.db = db; // Inject the mock db
//        cakeCartDao.myDBHelper = dbHelper; // Inject the mock dbHelper
//    }
//
//    @Test
//    public void testInsert() {
//        Cake cake = new Cake(1, "CakeName", "Introduce", 100.0f, "Picture", 1, 1);
//        ContentValues values = new ContentValues();
//        values.put("cakeId", cake.getCakeId());
//        values.put("cakeName", cake.getCakeName());
//        values.put("introduce", cake.getIntroduce());
//        values.put("price", cake.getPrice());
//        values.put("cakePicture", cake.getCakePicture());
//        values.put("typeId", cake.getTypeId());
//        values.put("num", 1);
//
//        when(db.insert("cart", null, values)).thenReturn(1L); // Simulate successful insert
//
//        cakeCartDao.insert(cake);
//
//        verify(db).insert("cart", null, values);
//    }
//
//    @Test
//    public void testIsExit() {
//        when(db.query("cart", new String[]{"cakeId"}, "cakeId=?", new String[]{"1"}, null, null, null, null))
//                .thenReturn(createMockCursor(true)); // Simulate the existence of the cake
//
//        boolean result = cakeCartDao.isExit(new Cake(1, "CakeName", "Introduce", 100.0f, "Picture", 1, 1));
//
//        assertTrue(result);
//    }
//
//    @Test
//    public void testGetTotalPrice() {
//        Cursor mockCursor = createMockCursorForTotalPrice();
//        when(db.query("cart", null, null, null, null, null, null)).thenReturn(mockCursor);
//
//        float totalPrice = cakeCartDao.getTotalPrice();
//
//        assertEquals(300.0f, totalPrice);
//    }
//
//    private Cursor createMockCursor(boolean hasData) {
//        Cursor mockCursor = mock(Cursor.class);
//        when(mockCursor.getCount()).thenReturn(hasData ? 1 : 0);
//        when(mockCursor.moveToNext()).thenReturn(hasData);
//        if (hasData) {
//            when(mockCursor.getInt(0)).thenReturn(1);
//            when(mockCursor.getString(1)).thenReturn("CakeName");
//            when(mockCursor.getString(2)).thenReturn("Introduce");
//            when(mockCursor.getFloat(3)).thenReturn(100.0f);
//            when(mockCursor.getString(4)).thenReturn("Picture");
//            when(mockCursor.getInt(5)).thenReturn(1);
//            when(mockCursor.getInt(6)).thenReturn(1);
//        }
//        return mockCursor;
//    }
//
//    private Cursor createMockCursorForTotalPrice() {
//        Cursor mockCursor = mock(Cursor.class);
//        when(mockCursor.getCount()).thenReturn(3);
//        when(mockCursor.moveToNext()).thenReturn(true, true, true, false);
//        when(mockCursor.getInt(0)).thenReturn(1, 2, 3);
//        when(mockCursor.getString(1)).thenReturn("CakeName1", "CakeName2", "CakeName3");
//        when(mockCursor.getString(2)).thenReturn("Introduce1", "Introduce2", "Introduce3");
//        when(mockCursor.getFloat(3)).thenReturn(100.0f, 100.0f, 100.0f);
//        when(mockCursor.getString(4)).thenReturn("Picture1", "Picture2", "Picture3");
//        when(mockCursor.getInt(5)).thenReturn(1, 1, 1);
//        when(mockCursor.getInt(6)).thenReturn(1, 1, 1);
//        return mockCursor;
//    }
//}