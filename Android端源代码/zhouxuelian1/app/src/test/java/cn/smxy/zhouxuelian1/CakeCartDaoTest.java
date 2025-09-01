//package cn.smxy.zhouxuelian1;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue; // 确保导入了 assertTrue 方法
//import static org.mockito.Mockito.*;
//
//import cn.smxy.zhouxuelian1.dao.CakeCartDao;
//import cn.smxy.zhouxuelian1.entity.Cake;
//import cn.smxy.zhouxuelian1.utils.MyDBHelper;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CakeCartDaoTest {
//
//    @Mock
//    private SQLiteDatabase db;
//    @Mock
//    private MyDBHelper dbHelper;
//    private CakeCartDao cakeCartDao;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.openMocks(this); // 从 Mockito 4 开始，使用 openMocks 替代 initMocks
//        when(dbHelper.getWritableDatabase()).thenReturn(db);
//        cakeCartDao = new CakeCartDao(dbHelper);
//    }
//
//    @Test
//    public void testInsert_WhenCakeNotExist_ShouldInsertNewRecord() {
//        Cake cake = new Cake(1, "CakeName", "Introduce", 100.0f, "Picture", 1, 1);
//        ContentValues values = new ContentValues();
//        values.put("cakeId", cake.getCakeId());
//        values.put("num", 1);
//
//        when(db.insert("cart", null, values)).thenReturn(1L);
//        cakeCartDao.insert(cake);
//        verify(db).insert("cart", null, values);
//    }
//
//    @Test
//    public void testIsExit_WhenCakeExists_ShouldReturnTrue() {
//        Cursor mockCursor = mock(Cursor.class);
//        when(mockCursor.getCount()).thenReturn(1);
//        when(db.query("cart", eq(new String[]{"cakeId"}), eq("cakeId=?"),
//                eq(new String[]{"1"}), null, null, null, null)).thenReturn(mockCursor);
//
//        boolean result = cakeCartDao.isExit(new Cake(1, "", "", 0, "", 0, 0));
//        assertTrue(result);
//        verify(db).query("cart", eq(new String[]{"cakeId"}), eq("cakeId=?"),
//                eq(new String[]{"1"}), null, null, null, null);
//    }
//
//    @Test
//    public void testGetTotalPrice_WhenThreeCakes_ShouldSumCorrectly() {
//        Cursor mockCursor = mock(Cursor.class);
//        when(mockCursor.moveToNext()).thenReturn(true, true, true, false);
//        when(mockCursor.getFloat(3)).thenReturn(100.0f, 100.0f, 100.0f);
//        when(mockCursor.getInt(6)).thenReturn(1, 1, 1);
//        when(db.query("cart", null, null, null, null, null, null)).thenReturn(mockCursor);
//
//        float totalPrice = cakeCartDao.getTotalPrice();
//        assertEquals(300.0f, totalPrice, 0.001f);
//        verify(mockCursor, times(3)).moveToNext();
//    }
//}